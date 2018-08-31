package contract;

import com.oxchains.themis.common.ethereum.ContractHandler;
import com.oxchains.themis.common.ethereum.Web3jHandler;
import com.oxchains.themis.common.util.EncryptUtils;
import com.oxchains.themis.common.util.JPushUtils;
import com.oxchains.themis.common.util.JsonUtil;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.CipherException;
import org.web3j.protocol.core.methods.response.EthBlock;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ccl
 * @time 2018-05-15 15:41
 * @name TestHandler
 * @desc:
 */
public class TestHandler {
    private static String basePath = "D:\\temp\\ethereum\\";

    private static String admin = "0xac9f877bd62a9806dcc863f85e631ad0853056c1";

    private static String trustee01 = "0xfee6d006f135719d7d3546953d8921a7ed0c61a0";
    private static String trustee02 = "0xa27c3b6e23d1541b92fd835cc85aed54f748212e";
    private static String trustee03 = "0x3cbcd06204c1df807f942f9edab069934fc14140";
    private static String trustee04 = "0x2138fd90beb6d2ebbe8b4cbf4d6bf80fe8dd9cc9";
    private static String trustee05 = "0xaa7e1ef02efb5401733aff1b72d95c3c337d197f";

    private static String test01 = "0x85ffef6d7134f27c97179b26f2b1d7c026dcf87b";

    private static String buyer = "0xd518bb3c512235f7284a4d389e48104d99e35321";
    private static String seller = "0x18942629d3ba5545e0f3eaf52217360846b56722";

    public static void main(String[] args) throws IOException {
        BigInteger fame = new BigInteger("1");
        BigInteger deposit = new BigInteger("1");
        String publicKey = "ANYTHING";

        BigInteger hostNum = new BigInteger("3");

        ContractHandler truadminHander = new ContractHandler(basePath + "admin.json", "123456", null,2);
        ContractHandler trusteeHander = new ContractHandler(basePath + "trustee01.json", "123456", null,2);

//        truadminHander.addTrustee("0xd518bb3c512235f7284a4d389e48104d99e35321", fame, publicKey);
        //truadminHander.removeTrustee(trustee01);
        //truadminHander.updateTrusteeFame(trustee01, new BigInteger("3"));//0xee92cec4d5ac76a1e19bd8d94a57e98baddf84859f572cc8b288fa42df704fae
        //trusteeHander.increaseDeposit(new BigInteger("1"));//0xca646cf2849d41ccaa0e307a007ed4961e0d4d05d7825cc2e52a75244d566089
        //trusteeHander.decreaseDeposit(new BigInteger("1"));//0x885c664e0c75d8da338a96b56033e9f4cc6c7cdf1fc2d3135cec613bfd0b3351
        //trusteeHander.updatePublicKey("1100110011");//0xb7ea0a28671bdf4568affc04c705e1a2d4d3fa3de6e465eee3c9ee2c12ab4e0f
//        System.out.println(truadminHander.getTrusteeInfo(trustee01));
//         truadminHander.getTrustees(5);//0x5181cd8a6dbe17906f45a78551f1910aaf9716f9ea9e6d685a2b6e039bb98979
        //System.out.println("**********" +truadminHander.isTrustee(trustee05));
        //System.out.println(truadminHander.getTransactionStatus("0x5181cd8a6dbe17906f45a78551f1910aaf9716f9ea9e6d685a2b6e039bb98979"));

        //String orderId = "201806051435193548455";//canceled
        //String orderId = "201806051435193548456";//normal finished
        String orderId = "921613129";//arbitrate
        //String orderId = "201806061016235677625";//test
        Long buyerId = 71L;
        Long sellerId = 102L;
        ContractHandler traadminHandler = new ContractHandler(basePath+"admin.json", "123456", null ,1);
        // 更新托管合约
//        String result = traadminHandler.updateTrusteeContract("0x73dc1dc73d4395fcc7afe234ebb606aa09236b44");
//        System.out.println("----------------------" + result);
        ContractHandler buyerHandler = new ContractHandler(basePath+"buyer.json", "123456", null,1);
        ContractHandler arbiHandler = new ContractHandler(basePath+"trustee01.json", "123456", null,1);

        // 创建订单
       // traadminHandler.createNewTradeOrder(orderId, buyerId, 0, new BigInteger("1"));
        // 取消订单
//        String hash = traadminHandler.cancelTrade(orderId, buyerId);
//        System.out.println("cancelHash=" + hash);
      //  traadminHandler.confirmTradeOrder(orderId, 22L, new BigInteger("1"));//0x673eb57053414addc2acfb20e66137518dbbc8f0e9325ea7e2b07496af277cfd

        //traadminHandler.uploadSecret(orderId, "qaz123,wsx456,edc789,112233,445566",buyerId);//0xa2de5dff8dde2cbee807f9fcb8031aa744d1ff0612d1cd01c088b7e186949fea
        //traadminHandler.uploadSecret(orderId, "123qaz,456wsx,789edc,332211,665544",sellerId);
        //traadminHandler.finishOrder(orderId);

        //buyerHandler.getSecret(orderId, trustee05, sellerId);
        //traadminHandler.getOrderBuyer(orderId);
        //traadminHandler.getOrderSeller(orderId);
//        traadminHandler.getOrderTrustees(orderId);
        //traadminHandler.isOrderTrustee(orderId, trustee01);
//        traadminHandler.addArbitrator(admin);
        //traadminHandler.removeArbitrator(trustee01);

        // 是不是仲裁节点
//        System.out.println("isArbitrator =" + traadminHandler.isArbitrator("0xd518bb3c512235f7284a4d389e48104d99e35321"));
        // 是不是托管节点
//        System.out.println("isTrustee = " + trusteeHander.isTrustee(trustee01));
        // 申请仲裁
//        traadminHandler.arbitrate(orderId, 71L);
        //arbiHandler.judge(orderId, buyerId);

//        traadminHandler.getRequester(orderId);
        //traadminHandler.updateDefaultTrusteeNumber(new BigInteger("3"));
        //traadminHandler.getWinner(orderId);
        // 查询状态
        traadminHandler.getTransactionStatus("0x8771a2173b450104c7d6bb9b5af83668dd50a2febc2153cc52c73edd4a801d97");
//        System.out.println(DateUtil.getOrderId());
        // 上传碎片
//        String hash = traadminHandler.uploadSecret(orderId, "xxxx", buyerId);
//        System.out.println("++++"+hash);
        // 托管节点执行验证
//        ContractHandler trHander = new ContractHandler(basePath + "trustee01.json", "123456", null,1);
//        String hash = trHander.sendVerifyResult(orderId, true, true);
//        System.out.println("hash = " + hash);

        //System.out.println(EncryptUtils.encodeBase64("123456"));
        //System.out.println(EncryptUtils.decodeBase64("test1234560"));

        //System.out.println("201806061016235677625".length());
        //System.out.println("0xff4bd959dbabc76b313e4b1526cfd8e02fdedd9391d57bf6a01a9060a03bd919".length());

        int a = 10;
        BigInteger b = new BigInteger("30");
        System.out.println(b.add(new BigInteger("-" + a)));

        //testParseAddress();

        //admin 0xac9f877bd62a9806dcc863f85e631ad0853056c1   ---balance 100000000000
        //trustee01 0xfee6d006f135719d7d3546953d8921a7ed0c61a0   ---balance 0
        List<BigInteger> list = new ArrayList<>();
        list.add(new BigInteger("12345"));
        list.add(new BigInteger("178"));
        list.add(new BigInteger("3"));
        list.add(new BigInteger("5"));
        list.add(new BigInteger("3366"));

        System.out.println(list.contains(new BigInteger("12345")));

        Object x = null;
        BigInteger s = (BigInteger) x;
        System.out.println(s);

        //System.out.println( EncryptUtils.encodeBase64("123456"));

//        testAccount();
        //System.out.println("10000000000000000000".length());
        try {
            //testTransfer();
            // 根据订单查询状态
//            int status = traadminHandler.getOrderStatus("36634455");
//            System.out.println(status);
//            List<String> strList = traadminHandler.getOrderTrustees("47983484");
//            System.out.println(11);
//            String h = traadminHandler.uploadSecret(orderId,"ss,ss,ss,sssx,xx", 22L, "sss");
//            System.out.println("hash="+h);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void testAccount(){
        BigDecimal balance = null;
        try {
            String themisAdmin = basePath + "my-token-admin.json";//themis$1
          //  balance = Web3jHandler.getInstance("http://35.234.44.47:8545").getBalance(admin);
            BigDecimal amount = new BigDecimal("9900");
            String hash = Web3jHandler.getInstance("http://192.168.1.195:8545").transfer(themisAdmin, "themis$1", admin , amount);

            System.out.println(trustee01+ " --- balance ---: " + balance);
           //BigInteger noncePending = Web3jHandler.getInstance("http://192.168.1.195:8545").getNoncePending("0x9d2DE6DD3313e54C1902C7715dCdD5D16cf6aC77");
           //BigInteger nonce = Web3jHandler.getInstance("http://192.168.1.195:8545").getNonceLatest("0x9d2DE6DD3313e54C1902C7715dCdD5D16cf6aC77");
            //Web3jHandler.getInstance("http://192.168.1.195:8545").transfer(basePath+"admin.json", "123456", "0xfee6d006f135719d7d3546953d8921a7ed0c61a0", new BigDecimal("1"));//0x5e5b0fb77e3dd36ded1dec61baec0fb71816ae924a55ab49a085544e110701ac
            //System.out.println(JsonUtil.toJson(Web3jHandler.getInstance("http://192.168.1.195:8545").getTransactionByHash("0x5e5b0fb77e3dd36ded1dec61baec0fb71816ae924a55ab49a085544e110701ac").toString()));
            //System.out.println("nonce: " + nonce);
            //System.out.println("noncePending: " + noncePending);

            EthBlock.Block block = Web3jHandler.getInstance("http://192.168.1.195:8545").getBlock(new BigInteger("0"));
            System.out.println(block);


            System.out.println(Long.valueOf("0x5b371c60", 16));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testTransfer() throws Exception {
        BigDecimal amount = new BigDecimal("1000000");
        String[] addrs = {"0xb35402bcf369d7e37032975683ebe5b27e6a0d17",
                "0x31210b5055f71ba35d112ccef74c0326b5ce23c8",
                "0x1fdf46293c5d57818259f5e5b3cecc7bcf9df764",
                "0xa443ce75c231eb7a64f29dfde87ba17f0f98ab7a",
                "0xa86252e704d70f003fe78b535d45f41f0bc1296e",
                "0x735123daee7f7173e4841de36f6a2fb5d5c813cd",
                "0x9f930f8306a96ca7e8e15fc1ba7f280494a67e45",
                "0xf8d96a2fa81fe04da66698d003c0e862611a1a25",
                "0x8d3433d4be24dc9740e7985b2fa7570a83b0e77f",
                "0x96b5aea9c835546c6147477b98cac9a95585048b"};
        String themisAdmin = basePath + "my-token-admin.json";
        List<String> hashes = new ArrayList<>(addrs.length);
        for(String addr : addrs){
            String hash = Web3jHandler.getInstance("http://192.168.1.195:8545").transfer(themisAdmin, "themis$1", addr , amount);
            hashes.add(hash);
        }
        System.out.println(hashes);
    }
    public static void testParseAddress(){

        String address = null;
        try {
            address = Web3jHandler.parseAddressFromExtra(Web3jHandler.getInstance("http://192.168.1.195:8545").getLatestBlock());
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(address);
    }
}
