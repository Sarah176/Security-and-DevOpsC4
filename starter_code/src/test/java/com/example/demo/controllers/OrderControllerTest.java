package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderControllerTest {
    private OrderController orderController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup(){
        orderController = new OrderController();
        TestUtils.injectObjects(orderController,"userRepository",userRepository);
        TestUtils.injectObjects(orderController,"orderRepository",orderRepository);
    }
    @Test
    public void submitOrder(){
        when(userRepository.findByUsername("test")).thenReturn(getUser());

        final ResponseEntity<UserOrder> response = orderController.submit("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        UserOrder order = response.getBody();
        Assert.assertEquals(order.getUser().getId(),getUser().getId());
        Assert.assertTrue(order.getItems().contains(getItem1()));


        final ResponseEntity<UserOrder> response2 = orderController.submit("test2");
        Assert.assertEquals(404,response2.getStatusCodeValue());

    }
    @Test
    public void getOrdersForUser(){
        User user = getUser();
        when(userRepository.findByUsername("test")).thenReturn(user);
        when(orderRepository.findByUser(user)).thenReturn(getUserOrder());

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertFalse(response.getBody().isEmpty());

        final ResponseEntity<List<UserOrder>> response2 = orderController.getOrdersForUser("test2");
        Assert.assertEquals(404,response2.getStatusCodeValue());
    }
    private User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        Cart cart= new Cart();
        cart.setUser(user);
        cart.addItem(getItem1());
        user.setCart(cart);
        return user;
    }
    private Item getItem1(){
        Item item = new Item();
        item.setId(1L);
        item.setName("item 1");
        item.setDescription("test item");
        item.setPrice(BigDecimal.valueOf(10));
        return item;
    }
    private List<UserOrder> getUserOrder(){
        UserOrder userOrder= new UserOrder();
        User user = getUser();
        userOrder.setUser(user);
        userOrder.setItems(user.getCart().getItems());
        userOrder.setTotal(user.getCart().getTotal());
        userOrder.setId(1L);
        return Arrays.asList(userOrder);
    }
}
