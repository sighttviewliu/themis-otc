package com.oxchains.themis.common.util;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by huohuo on 2017/10/24.
 */
public class DateUtil {
    private static final long DIFF_SECOND = 1000L;
    private static final long DIFF_MINUTE = 60000L;
    private static final long DIFF_HOUR = 3600000L;
    private static final long DIFF_DAY = 86400000L;
    private static final long DIFF_DAY_30 = 2592000000L;
    private static final long DIFF_DAY_31 = 2678400000L;
    private static final long DIFF_DAY_365 = 31536000000L;
    /**
     * 线程安全的日期格式对象
     */
    private static final ThreadLocal<DateFormat> YYMMDDHHMMSSSSS = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            // 完整日期
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df;//new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }

    };
    private static final ThreadLocal<DateFormat> YYMMDDHHMMSS = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            // 完整日期
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            df.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            return df;//new SimpleDateFormat("yyyyMMddHHmmssSSS");
        }

    };
    /**
     * 线程安全的日期格式对象
     */
    private static final ThreadLocal<DateFormat> DATE_FORMAT = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            // 完整日期
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }

    };

    /**
     * 线程安全的日期格式对象
     */
    private static final ThreadLocal<DateFormat> YMD = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            // 年月日
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    public static String formatTime(Date time) {
        return YYMMDDHHMMSSSSS.get().format(time);
    }

    /*
     * 获取当前时间精确到毫秒
     * */
    private static String getPresentTime() {
        return YYMMDDHHMMSSSSS.get().format(new Date());
    }

    /*
     * 获取当前时间格式为 YY-MM-dd HH:mm:ss
     * */
    public static String getPresentDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String long2TimeString(Long time){
        if(null == time){
            return null;
        }
        return DATE_FORMAT.get().format(new Date(time));
    }
    public static String getOrderId() {
        Random r = new Random();
        String randomStr = "" + r.nextInt(9) + r.nextInt(9) + r.nextInt(9) + r.nextInt(9);
        return getPresentTime() + randomStr;
    }

    public static String getTimestampStr(Date date){
        return YYMMDDHHMMSS.get().format(date);
    }
    public static String getDiffTimeString(Long time, long scale){
        if(null == time){
            return null;
        }
        String str = "";
        long diff = System.currentTimeMillis() - time * scale;
        if(diff < DIFF_MINUTE){
            //return (diff / DIFF_SECOND) + "s";
            return "1m";
        }
        long year = diff / DIFF_DAY_365;
        if(year > 0){
            str= year + "y";
            diff = diff % DIFF_DAY_365;
        }
        long month = diff / DIFF_DAY_30;
        if(month > 0){
            str = str + month + "M";
            diff = diff % DIFF_DAY_30;
        }
        long day = diff / DIFF_DAY;
        if(day > 0){
            str = str + day + "d";
            diff = diff % DIFF_DAY;
        }
        long hour = diff / DIFF_HOUR;
        if(hour > 0){
            str = str + hour + "h";
            diff = diff % DIFF_HOUR;
        }
        long miniute = diff / DIFF_MINUTE;
        if(miniute > 0){
            str = str + miniute + "m";
            diff = diff % DIFF_MINUTE;
        }
//        long second = diff / DIFF_SECOND;
//        if(diff < DIFF_MINUTE){
//            str =  str + second + "s";
//        }
        return str;
    }

    public static Long getTimeStamp(String date){
        if (StringUtils.isBlank(date)){
            return null;
        }
        try {
            return DATE_FORMAT.get().parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
