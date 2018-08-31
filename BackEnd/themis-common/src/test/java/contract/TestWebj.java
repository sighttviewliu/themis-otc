package contract;

import com.oxchains.themis.common.ethereum.ContractHandler;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;

import java.io.IOException;
import java.math.BigInteger;
import java.security.interfaces.ECKey;
import java.util.Arrays;
import java.util.List;

/**
 * @author anonymity
 * @create 2018-08-30 17:01
 **/
public class TestWebj {
    private static String trustee01 = "0xfee6d006f135719d7d3546953d8921a7ed0c61a0";
    private static String trustee02 = "0xa27c3b6e23d1541b92fd835cc85aed54f748212e";
    private static String trustee03 = "0x3cbcd06204c1df807f942f9edab069934fc14140";
    private static String trustee04 = "0x2138fd90beb6d2ebbe8b4cbf4d6bf80fe8dd9cc9";
    private static String trustee05 = "0xaa7e1ef02efb5401733aff1b72d95c3c337d197f";

    private static String trusteePath = "D:\\temp\\ethereum\\trustee01.json";
    public static void main(String[] args) {
//     uploadPubKey();
        getPubKey(trustee01);
        //updateFame(trustee05);


    }

    static void uploadPubKey(){
        List<String> paths = Arrays.asList("D:\\temp\\ethereum\\trustee01.json"/*,
                "D:\\temp\\ethereum\\trustee02.json",
                "D:\\temp\\ethereum\\trustee03.json",
                "D:\\temp\\ethereum\\trustee04.json",
                "D:\\temp\\ethereum\\trustee05.json"*/);
        for(String path : paths){
            try {
                Credentials credentials = WalletUtils.loadCredentials("123456", path);
                ECKeyPair ecKeyPair = credentials.getEcKeyPair();
                BigInteger pubKeyInt = ecKeyPair.getPublicKey();

                ContractHandler trusteeHander = new ContractHandler(trusteePath, "123456", null,2);
                trusteeHander.updatePublicKey(pubKeyInt+ "");

                System.out.println(pubKeyInt.toString(16));
                System.out.println(pubKeyInt);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CipherException e) {
                e.printStackTrace();
            }
        }

    }

    static void getPubKey(String trustee){
        ContractHandler truadminHander = new ContractHandler( "D:\\temp\\ethereum\\admin.json", "123456", null,2);
        try {
            System.out.println(truadminHander.getTrusteeInfo(trustee));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void updateFame(String trustee){
        ContractHandler truadminHander = new ContractHandler( "D:\\temp\\ethereum\\admin.json", "123456", null,2);
        truadminHander.updateTrusteeFame(trustee, new BigInteger("9"));
    }

}
