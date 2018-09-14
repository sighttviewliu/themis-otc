package com.oxchains.themis.common.util;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.web3j.crypto.WalletUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @author ccl
 * @time 2018-08-21 10:31
 * @name CoinAddressValidateUtil
 * @desc:
 */
public class CoinAddressValidateUtil {

    public static boolean isETHValidAddress(String addr) {
        if (null == addr || "".equals(addr.trim()) || !addr.startsWith("0x")) {
            return false;
        }
        return WalletUtils.isValidAddress(addr);
    }
    public static boolean isBTCValidAddress(String addr, boolean mainnet) {
        try {
            NetworkParameters networkParameters = null;
            if (mainnet) {
                networkParameters = MainNetParams.get();
            } else {
                networkParameters = TestNet3Params.get();
            }
            Address address = Address.fromBase58(networkParameters, addr);
            if (address != null) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private final static String ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";
    /**
     * btc(bch,usdt)地址校验
     * return: true有效,false无效
     */
    public static boolean bitcoinAddressValidate(String addr) {
        if (null == addr || addr.length() < 26 || addr.length() > 35) {
            return false;
        }
        byte[] decoded = decodeBase58To25Bytes(addr);
        if (decoded == null){
            return false;
        }

        byte[] hash1 = sha256(Arrays.copyOfRange(decoded, 0, 21));
        byte[] hash2 = sha256(hash1);

        return Arrays.equals(Arrays.copyOfRange(hash2, 0, 4), Arrays.copyOfRange(decoded, 21, 25));
    }
    private static byte[] decodeBase58To25Bytes(String input) {
        BigInteger num = BigInteger.ZERO;
        for (char t : input.toCharArray()) {
            int p = ALPHABET.indexOf(t);
            if (p == -1) {
                return null;
            }
            num = num.multiply(BigInteger.valueOf(58)).add(BigInteger.valueOf(p));
        }

        byte[] result = new byte[25];
        byte[] numBytes = num.toByteArray();
        System.arraycopy(numBytes, 0, result, result.length - numBytes.length, numBytes.length);
        return result;
    }
    private static byte[] sha256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
}
