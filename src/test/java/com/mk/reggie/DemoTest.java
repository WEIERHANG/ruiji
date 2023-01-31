package com.mk.reggie;


import org.junit.Test;

public class DemoTest {
    @Test
    public void test(){
//        String reg = "[\\u4e00-\\u9fa5]+";
//        String r1="12我的";
//        String r2="我";
//        boolean matches1 = r1.matches(reg);
//        boolean matches2 = r2.matches(reg);
//        System.out.println(matches1);
//        System.out.println(matches2);

        String test1="1mm";
        if(test1.indexOf("m")!=-1){
            String m = test1.replace("m", "");
            System.out.println(m);
        }
//        System.out.println(mm);
    }

}
