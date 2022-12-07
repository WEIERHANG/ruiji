package com.mk.reggie;

import org.junit.Test;

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
}
