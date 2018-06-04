package com.oxchains.themis.common.util;

import java.util.Random;

/**
 * 生成的组合为大写字母加数字。已经去除了容易让用户混淆的字符O、0、1，保留了I
 */
public class RandomUtil {
    public static String getCombination(int length) {
        String combination = "";
        String charOrNum = "char";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                charOrNum = "char";
            }else if (i == length-1) {
                charOrNum = "num";
            }else {
                charOrNum = random.nextInt(2)%2==0?"char":"num";
            }
            // 获得随机的数字
            int y = 0;
            if (charOrNum.equals("num")) {
                y = random.nextInt(10);
                if (y==0) { y=3; }  // 0和O不易区分
                if (y==1) { y=9; }  // 1和I不易区分
                combination += String.valueOf(y);
            }else {// 获得随机的 大写字母
                y = 65+random.nextInt(26);
                if (y==79) { y=75; }    // 0和O不易区分
                combination += (char) (y);
            }
        }
        return combination;
    }
    public static Long getRandomNumber(int length){
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int randomInt = random.nextInt(9) + 1;

            stringBuffer.append(String.valueOf(randomInt));

        }
        return Long.parseLong(stringBuffer.toString());
    }
}
