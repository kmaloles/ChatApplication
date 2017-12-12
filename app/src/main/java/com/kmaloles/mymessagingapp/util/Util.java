package com.kmaloles.mymessagingapp.util;

import android.content.Context;
import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kevinmaloles on 12/12/17.
 */

public class Util {

    public static class Dates {
        private static final String LONG_DATE_FORMAT = "EEE dd MMM yyyy hh:mm aaa";
        private static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
        private static final String BACKEND_DATE_FORMAT =  "yyyy-MM-dd'T'HH:mm:ss'Z'";
        // Gregorian
        private static final long AVG_MONTH_IN_MILLIS = 2629746000L;
        private static final String TIMEZONE_UTC = "UTC";


        public static String getCurrentTime(){
            SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_DATE_FORMAT);
            return dateFormat.format(new Date());
        }

        public static Date getDateFromString(String s){
            SimpleDateFormat dateFormat = new SimpleDateFormat(LONG_DATE_FORMAT);
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(s);
            } catch (ParseException e) {
                try{
                    //if long format fails, it use the other format
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
                    convertedDate = dateFormat2.parse(s);
                }catch (ParseException ex) {
                    ex.printStackTrace();
                }
            }
            return convertedDate;
        }

        public static Date getDateFromBackendString(String s){
            SimpleDateFormat dateFormat = new SimpleDateFormat(BACKEND_DATE_FORMAT);
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return convertedDate;
        }


        public static String getFormatedDate(Date date){
            DateFormat targetFormat = new SimpleDateFormat(LONG_DATE_FORMAT);
            String formattedDate = "";
            formattedDate = targetFormat.format(date);
            return formattedDate;
        }

        public static String timeSince(Context context, Date startTime) {
            long now = new Date().getTime(); // In milliseconds since epoch
            return DateUtils.getRelativeTimeSpanString(
                        startTime.getTime(),
                        now,
                        DateUtils.SECOND_IN_MILLIS, 0)
                        .toString();
        }
    }

}
