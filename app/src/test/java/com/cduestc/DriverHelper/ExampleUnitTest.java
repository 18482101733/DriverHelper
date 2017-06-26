package com.cduestc.DriverHelper;

import com.cduestc.DriverHelper.bean.User;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        System.out.println(isPhoneNum("18482101234"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        System.out.println(calendar.get(Calendar.MONTH));


        System.out.println();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date =  format.parse("2016-4-6");
        date.getTime();
        calendar.setTime(date);

        System.out.println("年 " + calendar.get(Calendar.YEAR));
        System.out.println("月 " + calendar.get(Calendar.MONTH));
        System.out.println("日 " + calendar.get(Calendar.DAY_OF_MONTH));


        User user = new User();
        System.out.println(user.getStates());

    }

    public static boolean isPhoneNum(String phoneNumber){
        Pattern cm = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher one = cm.matcher(phoneNumber);
        return one.matches() ;


    }
}