package com.mk.reggie;

import com.mk.reggie.entity.ShoppingCart;
import com.mk.reggie.service.impl.ShoppingCartServiceImp;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ShoppingCartServiceTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private Statement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private ShoppingCartServiceImp shoppingCartService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        shoppingCartService = new ShoppingCartServiceImp();
    }

    @Test
    public void testGetShoppingCartItems() throws Exception {
        // 模拟数据库连接和查询
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);

        // 模拟ResultSet数据
        when(mockResultSet.next()).thenReturn(true).thenReturn(false);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("name")).thenReturn("Test Item");
        when(mockResultSet.getLong("user_id")).thenReturn(123L);
        when(mockResultSet.getLong("dish_id")).thenReturn(456L);
        when(mockResultSet.getLong("setmeal_id")).thenReturn(789L);
        when(mockResultSet.getString("dish_flavor")).thenReturn("Spicy");
        when(mockResultSet.getInt("number")).thenReturn(2);
        when(mockResultSet.getDouble("amount")).thenReturn(19.99);
        when(mockResultSet.getString("image")).thenReturn("test_image.jpg");
        when(mockResultSet.getTimestamp("create_time")).thenReturn(new java.sql.Timestamp(System.currentTimeMillis()));

        // 调用服务方法
        List<ShoppingCart> shoppingCartItems = shoppingCartService.getShoppingCartItems(123L);

        // 验证结果
        assertNotNull(shoppingCartItems);
        assertEquals(1, shoppingCartItems.size());
        ShoppingCart item = shoppingCartItems.get(0);
        assertEquals(1L, item.getId().longValue());
        assertEquals("Test Item", item.getName());
        // 继续验证其他字段...
    }
}
