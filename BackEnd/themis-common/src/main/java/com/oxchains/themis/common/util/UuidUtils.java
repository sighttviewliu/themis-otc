package com.oxchains.themis.common.util;

import java.util.UUID;

/**
 * @author ccl
 * @time 2018-03-30 10:30
 * @name UuidUtils
 * @desc:
 */
public class UuidUtils {
    public static String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        return uuidStr;
    }


    public static String getUUID(int len){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String uuidStr=str.replace("-", "");
        if(len <=0 || len >=32){
            return uuidStr;
        }else {
            return uuidStr.substring(0,len);
        }
    }

}
