package com.example.figurechatcommunity.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

    public final static String FORMAT_TIME = "HH:mm";
    public final static String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    public final static String FORMAT_DATE_TIME_SECOND = "yyyy-MM-dd HH:mm:ss";
    public final static String FORMAT_MONTH_DAY_TIME = "MM-dd HH:mm";

    public static String getFormatToday(String dateFormat) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(currentTime);
    }

    public static Date stringToDate(String dateStr, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }
    public static String dateToString(Date date, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(date);
    }

    public static String getChatTime(boolean hasYear,long timesamp) {
        long clearTime = timesamp;
        String result;
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(clearTime);
        int temp = Integer.parseInt(sdf.format(today))
                - Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
            case 0:
                result = "今天 " + getHourAndMin(clearTime);
                break;
            case 1:
                result = "昨天 " + getHourAndMin(clearTime);
                break;
            case 2:
                result = "前天 " + getHourAndMin(clearTime);
                break;
            default:
                result = getTime(hasYear,clearTime);
                break;
        }
        return result;
    }

    public static String getTime(boolean hasYear,long time) {
        String pattern=FORMAT_DATE_TIME;
        if(!hasYear){
            pattern = FORMAT_MONTH_DAY_TIME;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(new Date(time));
    }

    public static String getHourAndMin(long time) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME);
        return format.format(new Date(time));
    }


    /**
     * @param time
     * @return 时间间隔
     */
    public static String dayToNow(long time) {
        Calendar now = Calendar.getInstance();
        long minute = (now.getTimeInMillis() - time) / 60000;
        if (minute < 60) {
            if (minute == 0) {
                return "刚刚";
            } else {
                return minute + "分钟前";
            }
        }
        long hour = minute / 60;
        if (hour < 24) {
            return hour + "小时前";
        }
        long day = hour / 24;
        if (day < 30) {
            return day + "天前";
        }
        long month = day / 30;
        if (month < 11) {
            return month + "个月前";
        }
        long year = month / 12;
        return year + "年前";
    }
}
