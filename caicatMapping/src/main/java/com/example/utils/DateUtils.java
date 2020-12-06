package com.example.utils;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author PingYu
 * @date 2019/11/15 9:52
 */
public class DateUtils {

    /**
     * 比较俩时间大小
     * @param staDate
     * @param endDate
     * @return
     * @throws ParseException
     */
    public static boolean compareToDate (String staDate,String endDate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMM");
        Date sta = simpleDateFormat.parse(staDate);
        Date end = simpleDateFormat.parse(endDate);

        if(sta.getTime() > end.getTime()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 对日期的【分钟】进行加/减
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }
}
