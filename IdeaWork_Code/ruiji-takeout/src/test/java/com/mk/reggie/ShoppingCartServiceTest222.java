package com.mk.reggie;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ShoppingCartServiceTest222 {
    @Test
    public void testShoppingCartServiceTest222(){
        //注册驱动
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        //获取连接
        String url="jdbc:mysql://127.0.0.1:3306/ruiji?characterEncoding=utf-8&useSSL=false&allowPublicKeyRetrieval=true";
        String user="root";
        String password="123";
        Connection conn = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            conn = DriverManager.getConnection(url, user, password);
            //创建statement对象
            statement = conn.createStatement();
            //执行查询语句
            String sql = "SELECT id, name, user_id, dish_id, setmeal_id, dish_flavor, number, amount, image, create_time FROM shopping_cart WHERE user_id = 1794622488341155842 ORDER BY create_time ASC";
            resultSet = statement.executeQuery(sql);

            //处理结果集
            while (resultSet.next()) {
                // 使用 long 来获取大的整数值
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                long userId = resultSet.getLong("user_id");
                long dishId = resultSet.getLong("dish_id");
                long setmealId = resultSet.getLong("setmeal_id");
                String dishFlavor = resultSet.getString("dish_flavor");
                int number = resultSet.getInt("number");
                double amount = resultSet.getDouble("amount");
                String image = resultSet.getString("image");
                java.sql.Timestamp createTime = resultSet.getTimestamp("create_time");

                // 打印或处理其他列的值
                System.out.println("ID: " + id + ", Name: " + name + ", User ID: " + userId + ", Dish ID: " + dishId +
                        ", Setmeal ID: " + setmealId + ", Dish Flavor: " + dishFlavor + ", Number: " + number +
                        ", Amount: " + amount + ", Image: " + image + ", Create Time: " + createTime);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            // 释放资源
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
