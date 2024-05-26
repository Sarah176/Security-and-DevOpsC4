package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.AssertTrue;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {
    private CartController cartController;
    private final UserRepository userRepository = mock(UserRepository.class);
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final CartRepository cartRepository = mock(CartRepository.class);
    @Before
    public void setup(){
        cartController = new CartController();
        TestUtils.injectObjects(cartController,"itemRepository",itemRepository);
        TestUtils.injectObjects(cartController,"userRepository",userRepository);
        TestUtils.injectObjects(cartController,"cartRepository",cartRepository);
    }
    @Test
    public void addToCart(){
        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem1()));
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(1L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.addTocart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Cart cart= response.getBody();
        Assert.assertTrue(cart.getItems().contains(getItem1()));

        request.setUsername("test2");
        final ResponseEntity<Cart> response2 = cartController.addTocart(request);
        Assert.assertEquals(404,response2.getStatusCodeValue());

        request.setUsername("test");
        request.setItemId(2L);
        final ResponseEntity<Cart> response3 = cartController.addTocart(request);
        Assert.assertEquals(404,response3.getStatusCodeValue());

    }
    @Test
    public void removeFromCart(){
        when(userRepository.findByUsername("test")).thenReturn(getUser());
        when(itemRepository.findById(2L)).thenReturn(Optional.of(getItem2()));
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("test");
        request.setItemId(2L);
        request.setQuantity(1);

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Cart cart= response.getBody();
        Assert.assertFalse(cart.getItems().contains(getItem2()));

        request.setUsername("test2");
        final ResponseEntity<Cart> response2 = cartController.removeFromcart(request);
        Assert.assertEquals(404,response2.getStatusCodeValue());

        request.setUsername("test");
        request.setItemId(3L);
        final ResponseEntity<Cart> response3 = cartController.removeFromcart(request);
        Assert.assertEquals(404,response3.getStatusCodeValue());

    }

    private User getUser(){
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        Cart cart= new Cart();
        cart.addItem(getItem2());
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
    private Item getItem2(){
        Item item = new Item();
        item.setId(2L);
        item.setName("item 2");
        item.setDescription("test item2");
        item.setPrice(BigDecimal.valueOf(10));
        return item;
    }

}
