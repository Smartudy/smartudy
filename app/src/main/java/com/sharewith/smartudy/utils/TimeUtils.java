package com.sharewith.smartudy.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Simjae on 2018-08-21.
 */

public class TimeUtils {

    public static String getPastTime(String time){
        String resulttime = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            long lasttime = format.parse(time).getTime();
            long curtime = format.parse(format.format(new Date())).getTime();
            int minute = (int)((curtime-lasttime) / 60000);
            int hour = minute/60;
            int day = hour/24;
            minute = minute % 60;
            if(hour == 0){
                resulttime = "방금 전";
            }
            else if(day == 0){
                if(minute != 0)
                    resulttime = hour +"h "+minute+"m";
                else
                    resulttime = hour + "h ";
            }
            else {
                resulttime = day + "day";
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("Question",resulttime+"에 등록된 게시글을 가져옴");
        return resulttime;
    }
}
