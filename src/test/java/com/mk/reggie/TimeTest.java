package com.mk.reggie;

import org.junit.Test;

public class TimeTest {
    @Test
    public void timeTest(){
        StringBuffer stringBuffer=new StringBuffer("20220211");
        stringBuffer.insert(4,"-");
        stringBuffer.insert(7,"-");
        System.out.println(stringBuffer);
    }
}
