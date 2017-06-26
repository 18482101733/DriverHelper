package com.cduestc.DriverHelper;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.format.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() throws ParseException {
        super(Application.class);


        String time = "2017-11-22";
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
        Date date = format.parse(time);
        DateUtils.isToday(System.currentTimeMillis());
        date.getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        System.out.println("year : "+calendar.get(Calendar.YEAR));
        System.out.println("month : "+calendar.get(Calendar.MONTH));
        System.out.println("day : "+calendar.get(Calendar.DAY_OF_MONTH));
        System.out.println("hour : " + calendar.get(Calendar.HOUR_OF_DAY));
        System.out.println("minute : " + calendar.get(Calendar.MINUTE));
        System.out.println("second : " + calendar.get(Calendar.SECOND));

        System.out.println(date.getTime() > System.currentTimeMillis());
        System.out.println(DateUtils.isToday(date.getTime()));
    }
}