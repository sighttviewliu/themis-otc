package com.oxchains.themis.common.util;

import com.oxchains.themis.common.okhttp.OkHttpClientHelper;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.bitcoinj.core.*;
import org.bitcoinj.script.Script;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ox
 * @time 2017-10-24 15:38
 * @name BitcoinService
 * @desc:
 */
@Slf4j
@Service
public class BitCoinjUtil {


    public static String signBtcTransactionData(@NonNull String from, @NonNull String to, @NonNull String privateKey, long value, long fee) throws Exception {

        NetworkParameters networkParameters = NetworkParameters.fromPmtProtocolID("test");
        Transaction transaction = new Transaction(networkParameters);
        DumpedPrivateKey dumpedPrivateKey = DumpedPrivateKey.fromBase58(networkParameters, privateKey);
        ECKey ecKey = dumpedPrivateKey.getKey();

        // 获取txHash和blockHeight和vout
        Map<String, String> map = getTxId(from);
        String txId = map.get("txId");
        String json = OkHttpClientHelper.get("https://api.blockcypher.com/v1/btc/test3/txs/" + txId, null, null);

        BtcTxInfo btcTxInfo = (BtcTxInfo) JsonUtil.fromJson(json, BtcTxInfo.class);
        List<BtcTxInfo.Outputs> outputsList = btcTxInfo.getOutputs();
        if (outputsList != null && !outputsList.isEmpty()){
            Map<String, String> voutMap = new HashMap<>();
            for (int i = 0; i < outputsList.size(); i++) {
                BtcTxInfo.Outputs outputs = outputsList.get(i);
                if (outputs.getAddresses().contains(from)) {
                    voutMap.put("vout", String.valueOf(i));
                    voutMap.put("script", outputs.getScript());
                }
            }

            long totalMoney = 0;
            List<UTXO> utxos = new ArrayList<>();

            UTXO utxoObj = new UTXO(Sha256Hash.wrap(txId), Long.valueOf(voutMap.get("vout")), Coin.valueOf(value), btcTxInfo.getBlock_height(), false, new Script(Hex.decode(voutMap.get("script"))));
            utxos.add(utxoObj);

            Coin v =  Coin.valueOf(value);
            transaction.addOutput(Coin.valueOf(value), Address.fromBase58(networkParameters, to));

            //消费列表总金额 - 已经转账的金额 - 手续费 就等于需要返回给自己的金额了
            long balance = totalMoney - value - fee;
            //输出-转给自己
            if (balance > 0) {
                transaction.addOutput(Coin.valueOf(balance), Address.fromBase58(networkParameters, from));
            }

            //输入未消费列表项
            for (UTXO utxo : utxos) {
                TransactionOutPoint outPoint = new TransactionOutPoint(networkParameters, utxo.getIndex(), utxo.getHash());
                transaction.addSignedInput(outPoint, utxo.getScript(), ecKey, Transaction.SigHash.ALL, true);
            }
        }

        return Hex.toHexString(transaction.bitcoinSerialize());

    }

    private static Map<String, String> getTxId(String address) throws Exception {
        Map<String, String> map = new HashMap<>(1);
        String json = OkHttpClientHelper.get("https://api.blockcypher.com/v1/btc/test3/addrs/" + address, null, null);
        BtcAddressInfo btcAddressInfo = (BtcAddressInfo) JsonUtil.fromJson(json, BtcAddressInfo.class);
        List<BtcAddressInfo.BtcTxRefs> btcTxRefsList = btcAddressInfo.getTxrefs();
        if (btcTxRefsList != null){
            if (btcTxRefsList.size() == 1) {
                for (BtcAddressInfo.BtcTxRefs txRefs : btcTxRefsList) {
                    map.put("txId", txRefs.getTx_hash());
                }
            } else {
                log.info("托管地址理论上只使用一次，此地址：{}是使用了多次", address);
                Integer height = btcTxRefsList.stream().mapToInt(obj -> obj.getBlock_height()).max().getAsInt();
                btcTxRefsList.stream().forEach(btcTxRefs -> {
                    if (btcTxRefs.getBlock_height().equals(height)){
                        map.put("txId", btcTxRefs.getTx_hash());
                    }
                });
            }
        }

        return map;
    }

    @Data
    public class BtcTxInfo {
        private String block_hash;
        private Integer block_height;
        private String hash;
        private List<String> addresses;
        private String total;
        private String fees;
        private String size;
        private String preference;
        private String relayed_by;
        private String confirmed;
        private String received;
        private String ver;
        private String lock_time;
        private String double_spend;
        private String vin_sz;
        private String vout_sz;
        private String confirmations;
        private String confidence;
        private List<Inputs> inputs;
        private List<Outputs> outputs;

        @Data
        public class Inputs {
            private String prev_hash;
            private String output_index;
            private String script;
            private String output_value;
            private String sequence;
            private List<String> addresses;
            private String script_type;
            private String age;
        }

        @Data
        public class Outputs {
            private String value;
            private String script;
            private String spent_by;
            private List<String> addresses;
            private String script_type;
        }
    }

    @Data
    public class BtcAddressInfo {
        private String address;
        private String total_received;
        private String total_sent;
        private String balance;
        private String unconfirmed_balance;
        private String final_balance;
        private String n_tx;
        private String unconfirmed_n_tx;
        private String final_n_tx;
        private List<BtcTxRefs> txrefs;
        private String tx_url;

        @Data
        public class BtcTxRefs {
            private String tx_hash;
            private Integer block_height;
            private String tx_input_n;
            private String tx_output_n;
            private String value;
            private String ref_balance;
            private String confirmations;
            private String confirmed;
            private String double_spend;

        }
    }
}