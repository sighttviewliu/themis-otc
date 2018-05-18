package com.oxchains.themis.common.ethereum;

import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.utils.Convert;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-17 10:06
 * @name TokenContract
 * @desc:
 */
public class TokenContract extends AbstractContract {

    public TokenContract(Credentials credentials) {
        this.credentials = credentials;
    }

    public TokenContract(String password, String source) {
        initCredentials(password, source);
    }

    /**
     * token contract
     */
    protected static final String GETTOKEN_CONTRACT_ADDRESS = "0xeBf96926b01b8eE1620F45234478746b6c886D17";

    /**
     * 批准押金额度
     * @param spender FEE_CONTRACT_ADDRESS
     * @param value
     * @return
     */
    public String approve(String spender, BigInteger value){
        value = Convert.toWei(value.toString(), Convert.Unit.ETHER).toBigInteger();
        List<Type> inparams = Arrays.asList(
                new Address(spender),
                new Uint256(value));
        Function function = new Function("approve",inparams, Collections.<TypeReference<?>>emptyList());
        String result = transaction(GETTOKEN_CONTRACT_ADDRESS,function, credentials);
        return result;
    }
}
