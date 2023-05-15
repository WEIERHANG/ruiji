package com.mk.reggie;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class jdbcTest {
    @Test
    private void testJdbc(){
        //注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取连接
        String url="jdbc:mysql://190.0.0.183:3306/crp?characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";
        String user="root";
        String password="HanLan@2020!";
        Connection conn= null;
        try {
            conn = DriverManager.getConnection(url,user,password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //创建statement对象
        Statement statement = null;
        try {
            statement = conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //试用statement执行语句
        String sql="delete  from job_information";
        try {
            long l = statement.executeLargeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //释放资源
        if(statement!=null){
            try {
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
