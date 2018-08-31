package com.oxchains.themis.common.util;

import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author oxchains
 * @time 2017-12-05 10:08
 * @name RegexUtils
 * @desc:
 */
public class RegexUtils {
    //国内电话
    public static final String REGEX_PHONE="^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";
    public static final String REGEX_MOBILEPHONE="^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6])|(17[0-3,5-9])|(18[0-3,5-9])|(19[8|9]))\\d{8}$";
    public static final String REGEX_EMAIL="^[a-z0-9A-Z]+[-|a-z0-9A-Z._]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$";

    public static final String REGEX_ZH="^[\\u0391-\\uFFE5]+$";
    public static final String REGEX_URL="^((http|https)://)?([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$ ";
    public static final String REGEX_NAME = "^[a-zA-Z][a-zA-Z_0-9]*$";
    public static final String REGEX_NAME_LEN15 = "^[a-zA-Z][a-zA-Z_0-9]{4,15}$";
    public static final String REGEX_NAME_LEN32 = "^[a-zA-Z][a-zA-Z_0-9]{5,31}$";
    //public static final String REGEX_BTC_ADDRESS = "^[a-zA-Z0-9]${25,33}";
    public static final String REGEX_POSITIVE_INTEGER = "^[1-9]\\d*$";

    public static final String REGEX_IMAGE = ".+(.JPEG|.jpeg|.JPG|.jpg|.png|.PNG|.bmp|.BMP|.gif|.tif)$";
    public static final String REGEX_VIDEO = "^[1-9]\\d*$";
    public static final String REGEX_BANKCARD = "^([1-9]{1})(\\d{14}|\\d{18})$";

    /**
     * 字母数字
     */
    //public static final String REGEX_PWD =  "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{8,16})$";

    public static final String REGEX_PWD =  "^(?![0-9]+$)(?![a-zA-Z]+$)(?!([^(0-9a-zA-Z)]|[\\(\\)])+$)([^(0-9a-zA-Z)]|[\\(\\)]|[a-zA-Z]|[0-9]){6,16}$";
    /**
     *  四选三
     */
    //public static final String REGEX_PWD =  "^(?![a-zA-Z]+$)(?![A-Z0-9]+$)(?![A-Z\\W_]+$)(?![a-z0-9]+$)(?![a-z\\W_]+$)(?![0-9\\W_]+$)[a-zA-Z0-9\\W_]{8,}$";

    //身份证号正则
    public static final String REGEX_ID_NUMBER = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
    //中文姓名正则
    public static final String REGEX_REAL_NAME ="^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$";

    public static final String REGEX_BTC_ADDRESS =  "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{26,34})$";

    public static final String REGEX_ETH_ADDRESS =  "^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{42})$";

    public static boolean match(String str ,String regex){
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
    private RegexUtils(){}

    /**
     * 校验银行卡卡号
     */
    public static boolean checkBankCard(String bankCard) {
        if(bankCard.length() < 15 || bankCard.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(bankCard.substring(0, bankCard.length() - 1));
        if(bit == 'N'){
            return false;
        }
        return bankCard.charAt(bankCard.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     * @param nonCheckCodeBankCard
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeBankCard){
        if(nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for(int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if(j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char)((10 - luhmSum % 10) + '0');
    }
}
