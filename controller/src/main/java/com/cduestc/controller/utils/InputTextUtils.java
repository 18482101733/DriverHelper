package com.cduestc.controller.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by c on 2017/3/9.
 */
public class InputTextUtils {


    public static boolean isPhoneNum(String phoneNumber){
        Pattern cm = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher one = cm.matcher(phoneNumber);
        return one.matches() ;
    }



}
