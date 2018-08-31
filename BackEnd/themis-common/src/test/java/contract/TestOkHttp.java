package contract;

import com.alibaba.fastjson.JSONObject;
import com.oxchains.themis.common.okhttp.OkHttpClientHelper;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ccl
 * @time 2018-06-14 15:38
 * @name JsonRpc
 * @desc:
 */
public class TestOkHttp {
    public static void main(String[] args) {
        Map<String, String> header = new HashMap<>();
//        header.put("Content-type","application/json");

//        Map<String, Object> param = new HashMap<>();
//        param.put("id", 1);
//        param.put("jsonrpc", "2.0");
//        param.put("method", "trustee_getDecryptSecret");
//        param.put("params", new Object[]{new BigInteger("20181339")});//20181339
//
//        String result = OkHttpClientHelper.post("http://192.168.1.101:8090", header, param);
//        String secret = JSONObject.parseObject(result).get("result").toString();
//        System.out.println(result + "----------------------" + secret);
        //testBlock();
        testTransaction();
    }

    private static void testBlock(){
        String url = "https://chain.api.btc.com/v3/block/3";
        url = "https://chain.api.btc.com/v3/block/latest";
        String result = OkHttpClientHelper.get(url, null, null);
        System.out.println(result);
    }

    private static void testTransaction(){
//        String url = "https://chain.api.btc.com/v3/tx/0eab89a271380b09987bcee5258fca91f28df4dadcedf892658b9bc261050d96";
        String url = "http://192.168.1.195:9030/themis-order/order/arbitrate/10/1";
        String result = OkHttpClientHelper.get(url, null, null);
        System.out.println(result);
    }

    private static void testAddress(){
        String url = "https://chain.api.btc.com/v3/address/15urYnyeJe3gwbGJ74wcX89Tz7ZtsFDVew";
        String result = OkHttpClientHelper.get(url, null, null);
        System.out.println(result);
    }
}
