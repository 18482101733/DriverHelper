package com.cduestc.DriverHelper.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by c on 2017/3/9.
 */
public class InputTextUtils {

    public static boolean isIdCard(String idCard){

        Pattern pattern = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher matcher = pattern.matcher(idCard);
        return  matcher.matches();
    }

    public static boolean isPlate(String plate){

        Pattern pattern = Pattern.compile("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领A-Z]{1}[A-Z]{1}[A-Z0-9]{4}[A-Z0-9挂学警港澳]{1}$");
        Matcher matcher = pattern.matcher(plate);

        return matcher.matches();
    }

    public static boolean isPhoneNum(String phoneNumber){
        Pattern cm = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher one = cm.matcher(phoneNumber);
        return one.matches() ;
    }



}
