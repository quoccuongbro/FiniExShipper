package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by vinh on 7/6/16.
 */

public class StringUtils {

    public static String DATE_FORMAT_SERVER = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static String DATE_FORMAT_SERVER_02 = "yyyy-MM-dd'T'HH:mm:ss";
    public static String DATE_FORMAT_SHOW = "HH 'giờ' mm 'phút', 'ngày' dd 'tháng' MM";

    public static String getStringFromDate(String dateFormat, Date dateTime) {
        if (dateTime == null) return "";
        try {
            String date = new SimpleDateFormat(dateFormat).format(dateTime);
            return date;
        } catch (Exception ex) {
            return "";
        }
    }

    public static Date getDateFromString(String dateFormat, String date) {
        try {
            Date dat = new SimpleDateFormat(dateFormat).parse(date);
            return dat;
        } catch (ParseException e) {
        }
        return null;
    }

    public static String dateToDate(String date) {
        Date dat = getDateFromString(DATE_FORMAT_SERVER, date);
        if (dat == null) {
            dat = getDateFromString(DATE_FORMAT_SERVER_02, date);
        }
        if (dat == null) return "";
        return getStringFromDate(DATE_FORMAT_SHOW, dat);
    }

    public static String dataToFeauture(String date) {
        try {
            Date past = getDateFromString(DATE_FORMAT_SERVER, date);
            if (past == null) {
                past = getDateFromString(DATE_FORMAT_SERVER_02, date);
            }
            if (past == null) return "Đang tính";
            Date now = new Date();
            long time = past.getTime() - now.getTime();
            if (time <= 0){
                time = -time;
                String retur = "Kết thúc ";
                long day = TimeUnit.MILLISECONDS.toDays(time);
                long hours = TimeUnit.MILLISECONDS.toHours(time);
                long minu = TimeUnit.MILLISECONDS.toMinutes(time);
                if (day > 0) retur += day + " ngày ";
                if (hours > 0) retur += (hours % 24) + " giờ ";
                if (minu >= 0) retur += (minu % 60) + " phút ";
                retur += " trước";
                return retur;
            }
            String retur = "Còn ";
            long day = TimeUnit.MILLISECONDS.toDays(time);
            long hours = TimeUnit.MILLISECONDS.toHours(time);
            long minu = TimeUnit.MILLISECONDS.toMinutes(time);
            if (day > 0) retur += day + " ngày ";
            if (hours > 0) retur += (hours % 24) + " giờ ";
            if (minu >= 0) retur += (minu % 60) + " phút ";
            return retur;
        } catch (Exception j) {
        }
        return "Đang tính";
    }

    public static long dateTimeFromText(String date) {
        try {
            Date past = getDateFromString(DATE_FORMAT_SERVER, date);
            if (past == null) {
                past = getDateFromString(DATE_FORMAT_SERVER_02, date);
            }
            if (past == null)
                return 0;
            else return past.getTime();
        } catch (Exception j) {
            return 0;
        }
    }
}
