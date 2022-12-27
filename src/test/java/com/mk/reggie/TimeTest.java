package com.mk.reggie;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class TimeTest {
    @Test
    public void timeTest() {
        StringBuffer stringBuffer = new StringBuffer("20220211");
        stringBuffer.insert(4, "-");
        stringBuffer.insert(7, "-");
        System.out.println(stringBuffer);
    }

    @Test
    public void timeTest1() throws ParseException {
        String birth = "20220102";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyyMMdd");

        Date date = formatter2.parse(birth);
        String birthday = formatter.format(date);
        System.out.println(birthday);
        //输出2022/01/02
    }

    /**
     *  ZonedDateTime转String
     */
    @Test
    public void testTime5(){
        String startTime="1543593600000";
        String endTime="1672588799000";
        ZonedDateTime start = ZonedDateTime.ofInstant(new Date(Long.valueOf(startTime)).toInstant(), ZoneId.systemDefault());
        ZonedDateTime end = ZonedDateTime.ofInstant(new Date(Long.valueOf(endTime)).toInstant(), ZoneId.systemDefault());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00").withZone(ZoneId.of("Asia/Shanghai"));
        if(startTime != null){
            String format = start.format(dtf);
            System.out.println(format);
        }
    }

    /**
     * String转ZonedDateTime转LocalDate
     */
    @Test
    public void timeTest3() {
        //LocalDateTime练习
        String dateTimeStr = "2016-10-25";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        //没有匹配到内容就返回-1
        if(dateTimeStr.indexOf("-")!=-1){
            LocalDateTime from = LocalDate.from(df1.parse(dateTimeStr)).atStartOfDay();
            ZonedDateTime start = from.atZone(zoneId);
            System.out.println(start);
        }else{
            TemporalAccessor parse = df.parse(dateTimeStr);
           //String format = df1.format(parse);
            LocalDate from = LocalDate.from(parse);
            LocalDateTime startParse = from.atStartOfDay();
            ZonedDateTime start = startParse.atZone(zoneId);
            System.out.println(start);
        }
    }

    /**
     * 将时间戳转换为时间
     * 1543593600000
     * 1672588799000
     */
    @Test
    public void testTime4(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf("1543593600000"))));
        System.out.println(sd);
    }
}
