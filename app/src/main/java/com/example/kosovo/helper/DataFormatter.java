package com.example.kosovo.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DataFormatter {
    public static String getFormattedDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            long currentTimeMillis = calendar.getTimeInMillis();
            long dateMillis = date.getTime();

            long diffMillis = currentTimeMillis - dateMillis;
            long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diffMillis);
            long diffHours = TimeUnit.MILLISECONDS.toHours(diffMillis);
            long diffDays = TimeUnit.MILLISECONDS.toDays(diffMillis);

            if (diffMinutes < 60) {
                if (diffMinutes == 1) {
                    return "1 minute ago";
                } else {
                    return diffMinutes + " minutes ago";
                }
            } else if (diffHours < 24) {
                if (diffHours == 1) {
                    return "1 hour ago";
                } else {
                    return diffHours + " hours ago";
                }
            } else {
                if (diffDays == 1) {
                    return "yesterday";
                } else {
                    return diffDays + " days ago";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Vraća originalni string u slučaju greške
        }
    }
}
