package contract;

import org.testng.annotations.Test;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.admin.Admin;
import org.web3j.protocol.admin.methods.response.PersonalUnlockAccount;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;
import rx.Subscription;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;


public class IDCTokenTest {

    // special to your url(default http://localhost:8545)
    private final String url = "http://192.168.1.115:8545";

    Credentials credentials = null;
    //
    String tokenName = "IDC Token";
    String tokenSymbol = "IDC";
    BigInteger decimalUints = new BigInteger("18");
    BigInteger decaimalAmount = new BigInteger("10").pow(18);
    BigInteger sleepTime = new BigInteger("60");
    BigInteger startTime = GetTime().add(sleepTime);
    BigInteger lastingTime = new BigInteger("12000").multiply(new BigInteger("60"));
    BigInteger endTime = startTime.add(lastingTime);
    BigInteger totalSupply = new BigInteger("800000000").multiply(decaimalAmount);
    BigInteger rate = new BigInteger("7000");
    BigInteger capPerAddress = new BigInteger("10").multiply(decaimalAmount);

    BigInteger gasPrice = new BigInteger("0");
    BigInteger gasLimit = new BigInteger("9730456");

    // change to fit format of solidity now(length should be 10)
    public BigInteger GetTime() {
        return BigInteger.valueOf(System.currentTimeMillis()/1000);
    }

    public Web3j GetConnection(String url) {
        if ("".equals(url)) {
            return Web3j.build(new HttpService());
        }
        Web3j web3j = Web3j.build(new HttpService(url));
        return web3j;
    }

    public String DeployContract(Web3j web3j, Credentials credentials) throws Exception {
        IDCToken contract = IDCToken.deploy(
                web3j, credentials, gasPrice, gasLimit, tokenName, tokenSymbol, decimalUints,startTime, endTime, totalSupply, rate, capPerAddress, credentials.getAddress()
        ).send();
        String contractAddress = contract.getContractAddress();
        return contractAddress;
    }

    // default credentials for account 0
    public Credentials GetDefaultCredentials() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials("123", "owner.json");
        return credentials;
    }

    public Credentials GetAccount1() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials("123", "account1.json");
        return credentials;
    }

    public Credentials GetAccount2() throws Exception {
        Credentials credentials = WalletUtils.loadCredentials("123", "account2.json");
        return credentials;
    }

    public IDCToken before(Credentials credentials) throws Exception {
        Web3j web3j = GetConnection(url);
        String contractAddress =  DeployContract(web3j, credentials);
        IDCToken idc = IDCToken.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return idc;
    }

    public IDCToken load(String contractAddress, Credentials credentials) throws Exception {
        Web3j web3j = GetConnection(url);
        IDCToken idc = IDCToken.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
        return idc;
    }

    @Test
    public void TestForDeploy() throws Exception {
        String url = "https://mainnet.infura.io/DaBLkj5nv1pPFA3kRcGK";
        Web3j web3j = GetConnection(url);

        BigInteger gasPrice = new BigInteger("60000000000");
        BigInteger gasLimit = new BigInteger("5000000");


        String tokenName = "Themis";
        String tokenSymbol = "GET";
        BigInteger decimalUints = new BigInteger("18");
        BigInteger decaimalAmount = new BigInteger("10").pow(18);
        BigInteger wan = new BigInteger("10000");
        BigInteger totalSupply = new BigInteger("10").multiply(wan).multiply(wan).multiply(decaimalAmount);

        GEToken_sol_GEToken contract = GEToken_sol_GEToken.deploy(web3j, credentials, gasPrice, gasLimit, tokenName, tokenSymbol, decimalUints, totalSupply).send();
        String contractAddress = contract.getContractAddress();

//        credentials.getEcKeyPair().getPrivateKey().

        System.out.println("the contract address is :" + contractAddress);
        System.out.println("the contract tokenName is :" + contract.name().send());
        System.out.println("the contract tokenSymbol is :" + contract.symbol().send());
        System.out.println("the contract decimalUints is :" + contract.decimals().send());
        System.out.println("the contract totalSupply is :" + contract.totalSupply().send());
    }

    @Test
    public void GetTransfer() throws Exception {
        String url = "https://mainnet.infura.io/DaBLkj5nv1pPFA3kRcGK";
        Web3j web3j = GetConnection(url);

        BigInteger gasPrice = new BigInteger("30000000000");
        BigInteger gasLimit = new BigInteger("70000");
        GEToken_sol_GEToken contract = GEToken_sol_GEToken.load("0x60c68a87be1e8a84144b543aacfa77199cd3d024", web3j, credentials, gasPrice, gasLimit);

        String to = "0x71D87b041Ed9917A4a52fCBA6B21C23f4BB29CA8";
        BigInteger decaimalAmount = new BigInteger("10").pow(18);
        BigInteger wan = new BigInteger("10000");
        BigInteger value = new BigInteger("10").multiply(wan).multiply(wan).multiply(decaimalAmount);
        contract.transfer(to, value).send();

        BigInteger a = contract.balanceOf(to).send();
        System.out.println("balance of a is :" + a);
    }

    @Test
    public void GetChownOwner() throws  Exception {
        String url = "https://mainnet.infura.io/DaBLkj5nv1pPFA3kRcGK";
        Web3j web3j = GetConnection(url);

        BigInteger gasPrice = new BigInteger("30000000000");
        BigInteger gasLimit = new BigInteger("70000");
        GEToken_sol_GEToken contract = GEToken_sol_GEToken.load("0x60c68a87be1e8a84144b543aacfa77199cd3d024", web3j, credentials, gasPrice, gasLimit);

        String newOwner = "0x71D87b041Ed9917A4a52fCBA6B21C23f4BB29CA8";
        contract.transferOwnership(newOwner).send();

        String owner = contract.owner().send();
        System.out.println("owner is " + owner);
    }

    @Test
    public void TestInitialized() throws Exception {

        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        // test for initialized
        String actualCreator = idc.creator().send();
        assertEquals(actualCreator, credentials.getAddress());

        String actualTokenName = idc.name().send();
        assertEquals(actualTokenName, tokenName);

        String actualSymbol = idc.symbol().send();
        assertEquals(actualSymbol, tokenSymbol);

        BigInteger actualDecinalUints = idc.decimals().send();
        assertEquals(actualDecinalUints, decimalUints);

        BigInteger initIDC = idc.balanceOf(credentials.getAddress()).send();
        assertEquals(initIDC, totalSupply);

        BigInteger actualStartTime = idc.startTime().send();
        assertEquals(actualStartTime, startTime);

        BigInteger actualEndTime = idc.endTime().send();
        assertEquals(actualEndTime, endTime);

        BigInteger actualRate = idc.rate().send();
        assertEquals(actualRate, rate);

        BigInteger actualCapAddress = idc.capPerAddress().send();
        assertEquals(actualCapAddress, capPerAddress);

        String actualWallet = idc.wallet().send();
        assertEquals(actualWallet, credentials.getAddress());
    }

    @Test
    public void TestPause() throws Exception {

        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        // pause tokens
        idc.pause().send();

        boolean acutalState = idc.paused().send();
        assertEquals(acutalState, true);

        // unpause tokens
        idc.unpause().send();

        acutalState = idc.paused().send();
        assertEquals(acutalState, false);

        // can not be changed by not owner
        Credentials credentials1 = GetAccount1();
        IDCToken idc1 = before(credentials1);
    }

    @Test
    public void TestOwner() throws Exception {

        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        Credentials newCredentials = GetAccount1();

        idc.transferOwnership(newCredentials.getAddress()).send();
        String newOwner = idc.owner().send();
        assertEquals(newOwner, newCredentials.getAddress());
    }

    @Test
    public void TestMint() throws Exception {

        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        Credentials account1 = GetAccount1();
        BigInteger mintIDC = new BigInteger("10").multiply(decaimalAmount);

        BigInteger pre = idc.balanceOf(account1.getAddress()).send();
        idc.mint(account1.getAddress(), mintIDC).send();
        BigInteger after = idc.balanceOf(account1.getAddress()).send();

        assertEquals(after.subtract(pre), mintIDC);

        BigInteger newTotal = idc.totalSupply().send();
        assertEquals(newTotal, totalSupply.add(mintIDC));
    }

    @Test
    public void TestNormalTransfer() throws Exception {
        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        Credentials account1 = GetAccount1();

        BigInteger pre = idc.balanceOf(account1.getAddress()).send();


        BigInteger transferIDC = new BigInteger("10").multiply(decaimalAmount);
        TransactionReceipt transactionReceipt = idc.transfer(account1.getAddress(), transferIDC).sendAsync().get();
        String transId = transactionReceipt.getTransactionHash();

        //IDCToken.getTransferEvents(transactionReceipt);

        BigInteger after = idc.balanceOf(account1.getAddress()).send();
        assertEquals(after.subtract(pre), transferIDC);
    }

    @Test
    public void TestBurnTokens() throws Exception {
        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        BigInteger pre = idc.balanceOf(credentials.getAddress()).send();
        BigInteger preTotal = idc.totalSupply().send();

        BigInteger burnAmount = new BigInteger("7000").multiply(decaimalAmount);
        idc.burn(burnAmount).send();

        BigInteger after = idc.balanceOf(credentials.getAddress()).send();
        BigInteger afterTotal = idc.totalSupply().send();

        assertEquals(pre.subtract(after), burnAmount);
        assertEquals(preTotal.subtract(afterTotal), burnAmount);
    }

    @Test
    public void TestWhiteList() throws Exception {
        Credentials credentials = GetDefaultCredentials();
        IDCToken idc = before(credentials);

        Credentials acc = GetAccount1();

        idc.addWhiteList(acc.getAddress()).send();
    }

    @Test
    public void TestTransferEth() throws Exception {

        Credentials account1 = GetAccount1();
        Credentials account2 = GetAccount2();

        Web3j web3j = GetConnection(url);

        EthGetBalance ethGetBalance2 = web3j
                .ethGetBalance(account2.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        BigInteger preAccount2 = ethGetBalance2.getBalance();

        Transfer.sendFunds(
                web3j, account1, account2.getAddress(),
                BigDecimal.valueOf(1.0), Convert.Unit.ETHER)
                .send();
        BigInteger transerAmount = new BigInteger("1").multiply(decaimalAmount);

        EthGetBalance ethGetBalance4 = web3j
                .ethGetBalance(account2.getAddress(), DefaultBlockParameterName.LATEST)
                .sendAsync()
                .get();

        BigInteger afterAccount2 = ethGetBalance4.getBalance();

        // check account2 get right amount eth
        assertEquals(transerAmount, afterAccount2.subtract(preAccount2));
    }

    @Test
    public void TestGasPrice() throws Exception {
        String url = "http://192.168.1.102:8545";
        Web3j web3j = GetConnection(url);
        EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
        BigInteger gasPrice = ethGasPrice.getGasPrice();
        System.out.println(gasPrice);
    }

    @Test
    public void TestGetReciption() throws Exception {

        String url = "http://192.168.1.102:8545";

        String contractAddress = "0xdbe16aa3c77ff1bc391f2b311152b86610de1494";

        BigInteger gWei = new BigInteger("1000000000");
        BigInteger gasPrice = new BigInteger("30").multiply(gWei);
        BigInteger gasLimit = new BigInteger("100000");

        String from = "0xde342d474ee94d1212a6498fac134cb28623c324";
        String _to = "0xdbe16aa3c77ff1bc391f2b311152b86610de1494";
        BigInteger _value = new BigInteger("7000").multiply(decaimalAmount);
        String password = "liuruichao123";

        Admin web3jJ = Admin.build(new HttpService(url));
        PersonalUnlockAccount personalUnlockAccount = web3jJ.personalUnlockAccount(from, password).send();
        if (personalUnlockAccount.accountUnlocked()) {
            // send a transaction
            System.out.println("personal account unlock");
        }

        EthGetTransactionCount ethGetTransactionCount = web3jJ.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();

        BigInteger nonce = ethGetTransactionCount.getTransactionCount();

        System.out.println("none is :" + nonce);

        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(_to),
                        new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        Double a = 1.1;
        new BigInteger(a.toString());

        Transaction transaction = Transaction.createFunctionCallTransaction(
                from, nonce, gasPrice, gasLimit, contractAddress, new BigInteger("0"), encodedFunction);
        EthSendTransaction ethSendTransaction = web3jJ.ethSendTransaction(transaction).send();
        ethSendTransaction.getError();
        System.out.println(ethSendTransaction);
        String hash = ethSendTransaction.getTransactionHash();

        System.out.println(hash);
    }



    public BigInteger changeToBigInteger(BigDecimal value) {

        //BigInteger
        BigDecimal weiValue = Convert.toWei(value, Convert.Unit.ETHER);
        if (!Numeric.isIntegerValue(weiValue)) {
            throw new UnsupportedOperationException("Non decimal Wei value provided: " + value + " " + Convert.Unit.ETHER.toString() + " = " + weiValue + " Wei");
        } else {
            return weiValue.toBigIntegerExact().multiply(new BigInteger("7000"));
        }
    }

    @Test
    public void TestDecimal() {
        BigDecimal value = new BigDecimal("1.2");

        BigInteger res = changeToBigInteger(value);
        System.out.println(res);
    }

    @Test
    public void TestReadFile() {
        String filePath = "/home/ubuntu/gopath/src/genemail/mail";
        ReadFile(filePath);
    }

    // Send Idt to people
    @Test
    public void TestSendIDT() throws Exception {
//        String url = "https://mainnet.infura.io/DaBLkj5nv1pPFA3kRcGK";
        String url = "http://192.168.1.102:8545";
        Web3j web3j = GetConnection(url);


        String from = "0x8fac410fd14eea9f380cf9af2846a01c2dc6cb27";
        EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(from, DefaultBlockParameterName.LATEST).sendAsync().get();
//        BigInteger nonce = ethGetTransactionCount.getTransactionCount();
        String IDTContractAddr = "0x02C4C78C462E32cCa4a90Bc499bF411Fb7bc6aFB";
        BigInteger nonce = new BigInteger("740");

//        String send_100 = "idt_100";
//        String send_200 = "idt_200";
//        String send_300 = "idt_300";
        String send_88 = "idt_88";

        BigInteger value = Convert.toWei("88", Convert.Unit.ETHER).toBigInteger();
        nonce = SendIDTByFile(send_88, value, nonce, web3j, IDTContractAddr, credentials);

    }

    public BigInteger SendIDTByFile(String filePath, BigInteger value, BigInteger startNonce, Web3j web3j, String IDTContractAddr, Credentials credentials) throws Exception {
        List<String> toList = ReadFileList(filePath);
        BigInteger step = new BigInteger("0");
        // 30 Gwei
        BigInteger gasPrice = Convert.toWei("20", Convert.Unit.GWEI).toBigInteger();
        // 70000 limit
        BigInteger gasLimit = new BigInteger("70000");
        for (int i = 0; i < toList.size(); i++) {
            BigInteger tmpNonce = startNonce.add(step);
            String toAddr = toList.get(i);

            String txHash = SendRawTx(web3j, IDTContractAddr, tmpNonce, toAddr, value, credentials, gasPrice, gasLimit);

            System.out.println("nonce is:" + tmpNonce + ",send to "+ toAddr +", idt value is: " + value + ",txhash is :" + txHash);
            step = step.add(new BigInteger("1"));
        }

        return startNonce.add(step);
    }

    public List<String> ReadFileList(String filepath) {
        try {
            FileReader reader = new FileReader(filepath);
            BufferedReader br = new BufferedReader(reader);
            String str = null;

            List<String> list = new ArrayList<>();

            while((str = br.readLine()) != null) {
                list.add(str.trim());
            }

            br.close();
            reader.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> ReadFile(String filepath) {
        try{
            FileReader reader = new FileReader(filepath);
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            Set<String> whiteList = new HashSet<>();

            while((str = br.readLine()) != null) {
                whiteList.add(str.trim());
            }

            br.close();
            reader.close();
            return whiteList;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String SendRawTx(Web3j web3j, String contractAddress, BigInteger nonce, String to, BigInteger value, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) throws Exception {
        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(to),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, contractAddress, BigInteger.ZERO, encodedFunction);

        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        String hexValue = Numeric.toHexString(signedMessage);
        EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();
        String transactionHash = ethSendTransaction.getTransactionHash();
        return transactionHash;
    }

    public String sendTx(Web3j web3j, BigInteger nonce, String to, BigInteger value, String from, String contractAddress) throws Exception {

        Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(to),
                        new Uint256(value)),
                Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);

        Transaction transaction = Transaction.createFunctionCallTransaction(
                from, nonce, gasPrice, gasLimit, contractAddress, new BigInteger("0"), encodedFunction);

        EthSendTransaction ethSendTransaction = web3j.ethSendTransaction(transaction).send();
        String hash = ethSendTransaction.getTransactionHash();
        return hash;
    }

    public boolean checkWhiteList(String address) {
        try {
            String contractAddress = "0x02C4C78C462E32cCa4a90Bc499bF411Fb7bc6aFB";
            Web3j web3j = Web3j.build(new HttpService("http://192.168.1.102:8545"));;
            Credentials credentials = GetDefaultCredentials();
            IDCToken idc = IDCToken.load(contractAddress, web3j, credentials, gasPrice, gasLimit);
            boolean exist = idc.checkExist(address).send();
            return exist;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        System.out.println("should not be here");
        return false;
    }

    @Test
    public void Test() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.115:8545"));
        Function function = new Function("getFundAddr",
                Arrays.<Type>asList(new Uint256(1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        String data = FunctionEncoder.encode(function);
        Transaction ethCallTransaction = Transaction.createEthCallTransaction(null, "0x2fb95baf9fdc9b145f9561a097bcfd5c206bad4f", data);
        EthCall send = web3j.ethCall(ethCallTransaction, DefaultBlockParameterName.LATEST).send();
        String address = send.getResult();
        System.out.println(address);
    }

    @Test
    public void TestBlockListener() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.102:8545"));
        Subscription subscription = web3j.blockObservable(true).take(10).subscribe(ethBlock -> {
            //
            EthBlock.Block block = ethBlock.getBlock();
            List<EthBlock.TransactionResult> transactionResults = block.getTransactions();

            System.out.println("block: " + block.getNumber() + ";tx size " + transactionResults.size());

            for (int i=0; i<transactionResults.size(); i++) {
                org.web3j.protocol.core.methods.response.Transaction transaction = (org.web3j.protocol.core.methods.response.Transaction)transactionResults.get(i);

                String from = transaction.getFrom();
                String to = transaction.getTo();
                BigInteger value = transaction.getValue();
                System.out.println("from " + from + " to " + to + " value " +value);
            }
        }, Throwable::printStackTrace);

        TimeUnit.MINUTES.sleep(2);

        boolean is = subscription.isUnsubscribed();
        System.out.println(is);
    }

    @Test
    public void TestPendingTxListening() throws Exception {
        Web3j web3j = Web3j.build(new HttpService("http://192.168.1.102:8545"));
        EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameterName.PENDING, true).send();
        EthBlock.Block block = ethBlock.getBlock();
        block.getTransactions().stream().forEach(tx-> {
            org.web3j.protocol.core.methods.response.Transaction txX = (org.web3j.protocol.core.methods.response.Transaction)tx;
            System.out.println(txX.getBlockNumber());
            System.out.println(txX.getNonce());
        });


    }


    @Test
    public void TestPow() {
        double a = 2;
        double b = 256;
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(Math.pow(a, b)));
        System.out.println(bigDecimal.toPlainString());
    }

    @Test
    public void TestSecurity() {
        BigInteger value = new BigInteger("2").pow(255);
        List<String> serviceNodes = new ArrayList<>();
        serviceNodes.add("0xb4d30cac5124b46c2df0cf3e3e1be05f42119033");
        serviceNodes.add("0x0e823ffe018727585eaf5bc769fa80472f76c3d7");
        Function function = new Function(
                "batchTransfer",
                Arrays.<Type>asList(
                        new org.web3j.abi.datatypes.DynamicArray<Address>(
                                org.web3j.abi.Utils.typeMap(serviceNodes, Address.class)),
                        new Uint256(value)
                ),
                Collections.<TypeReference<?>>emptyList());
        String encodedFunction = FunctionEncoder.encode(function);
        System.out.println(encodedFunction);

    }

    @Test
    public void TestA() {
        String a = "000000000000000000000000000000000785ee10d5da46d900f436a000000000";
        String b = "0000000000000000000000000000000000000000000000000000000000000001";
    }

    @Test
    public void Testbb() throws Exception {
        BigInteger nonce = new BigInteger("124");
        BigInteger gasPrice = new BigInteger("10000");
        BigInteger gasLimit = new BigInteger("2000000");
        String to = "0xf5c5c22ed599ede4973cb3f7b3681d9e71be34b8";
        BigInteger value = new BigInteger("0");
        String data = "测试";
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice, gasLimit, to, value, data);

        Credentials credentials = WalletUtils.loadCredentials("123", "testNet.json");;
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);

        String hexValue = Numeric.toHexString(signedMessage);

        System.out.println(hexValue);
    }

}
