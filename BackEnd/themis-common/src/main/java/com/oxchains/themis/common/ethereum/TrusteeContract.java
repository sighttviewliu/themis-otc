package com.oxchains.themis.common.ethereum;

import com.oxchains.themis.common.util.JsonUtil;
import groovy.util.logging.Slf4j;
import lombok.Data;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ccl
 * @time 2018-06-04 15:53
 * @name Trustee
 * @desc:
 */
@Slf4j
public class TrusteeContract extends AbstractContract{
    /**
     * trustee contract
     */
    protected String CONTRACT_ADDRESS = "0x73dc1dc73d4395fcc7afe234ebb606aa09236b44";

    public TrusteeContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public TrusteeContract(String password, String source) {
        initCredentials(password, source);
    }

    public TrusteeContract(String password, String source, String contract) {
        initCredentials(password, source);
        this.CONTRACT_ADDRESS = contract;
    }

    public String addTrustee(String id, BigInteger fame,String publicKey){
        Function function = new Function("addTrustee",
                Arrays.asList(new Address(id),
                new Uint32(fame),
                new Utf8String(publicKey)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String removeTrustee(String id){
        Function function = new Function("removeTrustee",
                Arrays.asList(new Address(id)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String updateTrusteeFame(String id, BigInteger newFame){
        Function function = new Function("updateTrusteeFame",
                Arrays.asList(new Address(id), new Uint32(newFame)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String increaseDeposit(BigInteger value){
        Function function = new Function("increaseDeposit",
                Arrays.<Type>asList(), Collections.<TypeReference<?>>emptyList());
        value = new BigInteger(Convert.toWei(value.toString(), Convert.Unit.ETHER).toString());
        String result = transaction(CONTRACT_ADDRESS,function, credentials, value);
        return result;
    }

    public String decreaseDeposit(BigInteger amount){
        amount = new BigInteger(Convert.toWei(amount.toString(), Convert.Unit.ETHER).toString());
        Function function = new Function("decreaseDeposit",
                Arrays.<Type>asList(new Uint256(amount)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String updatePublicKey(String newKey){
        Function function = new Function("updatePublicKey",
                Arrays.<Type>asList(new Utf8String(newKey)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS,function, credentials);
        return result;
    }

    public String getTrusteeInfo(String who) throws IOException {
        Function function = new Function("getTrusteeInfo",Arrays.asList(new Address(who)), Arrays.asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}, new TypeReference<Utf8String>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        List<Type> types = FunctionReturnDecoder.decode(result,function.getOutputParameters());

        if(null != types && types.size()>0){
            BigInteger fame = (BigInteger) types.get(0).getValue();
            BigInteger deposit = (BigInteger) types.get(1).getValue();
            String publicKey = types.get(2).getValue().toString();
            Trustee trustee = new Trustee(fame, deposit, publicKey);
            result = JsonUtil.toJson(trustee);
        }
        return result;
    }

    public String getTrustees(Integer num) {
        List<String> addresses = null;
        Function function = new Function("getTrustees",Arrays.asList(new Uint256(num)), Collections.<TypeReference<?>>emptyList());
        String result = transaction(CONTRACT_ADDRESS, function, credentials);
        return result;
    }

    public boolean isTrustee(String who) throws IOException {
        Function function = new Function("isTrustee",Arrays.asList(new Address(who)),Arrays.asList(new TypeReference<Bool>() {}));
        String result = query(null,CONTRACT_ADDRESS,function);
        return hexToBoolean(result);
    }

    @Data
    static class Trustee{
        private BigInteger fame;
        private BigInteger deposit;
        private String publicKey;

        public Trustee() {
        }

        public Trustee(BigInteger fame, BigInteger deposit, String publicKey) {
            this.fame = fame;
            this.deposit = deposit;
            this.publicKey = publicKey;
        }
    }
}
