package com.example.demo.util;

import com.example.demo.converter.MyConverter;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class TimeUtils {


    //getResources().getString(R.string.date_picker_format),getResources().getString(R.string.timezone)
    //getResources().getString(R.string.date_format),getResources().getString(R.string.timezone)
    //返回当前时间的标准字符串格式
    public static String  getDateNow(String date_format,String timezone){
        SimpleDateFormat format=new SimpleDateFormat(date_format);
        format.setTimeZone(TimeZone.getTimeZone(timezone));
        return format.format(new Date());
    }

    public static Integer timeStampParseToMinus(Timestamp timestamp){
        Long time = timestamp.getTime();
        int ms = time.intValue();
        int minus=ms/1000/60;
        return minus;
    }

    public static Integer timeStampIntervalParseToDay(Timestamp timestamp1,Timestamp timestamp2){
        Long time = (Math.abs(timestamp1.getTime()-timestamp2.getTime()))/1000/60/60/24;
        int minus= MyConverter.longToInt(time);
        return minus;
    }

    public static Timestamp minusParseToTimeStamp(Integer minus){
        int ms=minus*60*1000;
        Long MS=(long)ms;
        return new Timestamp(MS);
    }
    public static String sToMs(int date) {
        StringBuilder MS=new StringBuilder();
        int i = date / 60;
        if(i>=10)
            MS.append(i);
        else
            MS.append("0"+i);
        MS.append(":");
        int j=date%60;
        if(j>=10)
            MS.append(j);
        else
            MS.append("0"+j);

        return MS.toString();
    }

    public static Timestamp returnMonthBeginTimestamp(Date date) {
        Calendar calendar=Calendar.getInstance();
        //  calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date time = calendar.getTime();
        return MyConverter.dateToTimeStamp(time);
    }
    public static Timestamp returnYearBeginTimestamp(Date date) {
        Calendar calendar=Calendar.getInstance();
        //  calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);//ToDo:测试是否是本年
        calendar.set(Calendar.MONTH,1);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MILLISECOND,0);
        Date time = calendar.getTime();
        return MyConverter.dateToTimeStamp(time);
    }
    public static Timestamp returnMonthDayOneTimestamp(Date date) {
        Calendar calendar=Calendar.getInstance();
        //  calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MILLISECOND,0);
        return MyConverter.dateToTimeStamp(calendar.getTime());
    }

    public static Timestamp addOneDay(Timestamp monthBeginTimestamp) {
        return MyConverter.longToTimestamp(monthBeginTimestamp.getTime()+(long)1000*3600*24);
    }

    //date picker传入需要year+(monthOfYear+1)+dayOfMonth
    public static class DatePickerDateConverter{
        private static SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        private static SimpleDateFormat monthDateFormat=new SimpleDateFormat("MM-dd");
        private static SimpleDateFormat timeFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        public DatePickerDateConverter(){
            dateFormat=new SimpleDateFormat("yyyy-MM-dd");
          //  dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        }
        public  Integer[]  parseDateArray(Timestamp date){
            String[] data = dateFormat.format(MyConverter.timeStampToDate(date)).split("-");
            return new Integer[]{Integer.valueOf(data[0]),Integer.valueOf(data[1])-1,Integer.valueOf(data[2])};
        }

        public  Timestamp  parseTimestamp(Integer[] date) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
            instance.set(date[0],date[1],date[2]);
            return MyConverter.dateToTimeStamp(instance.getTime());
        }

        public String  timestampToDateFormat(Timestamp date){
            return dateFormat.format(MyConverter.timeStampToDate(date));
        }

        public static String  dateFormat(Date date){
            return dateFormat.format(date);
        }

        public static String  stampDateFormatMonthDay(Timestamp date){
            return monthDateFormat.format(MyConverter.timeStampToDate(date));

        }
        public static String  timeFormat(Timestamp date){
            if(date!=null)
            return timeFormat.format(MyConverter.timeStampToDate(date));
            return null;
        }
        public static Date  dateParse(String date){
            try {
                return dateFormat.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    // calendar.setTime(new Date());
    public static Timestamp returnTodayBeginTimestamp(Date date){
        Calendar calendar=Calendar.getInstance();
      //  calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MILLISECOND,0);
        return MyConverter.dateToTimeStamp(calendar.getTime());

    }

    public static Timestamp returnTodayEndTimestamp(Date date) {
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        calendar.setTime(date);
        calendar.set(Calendar.SECOND,59);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MILLISECOND,999);
        return MyConverter.dateToTimeStamp(calendar.getTime());
    }
}
