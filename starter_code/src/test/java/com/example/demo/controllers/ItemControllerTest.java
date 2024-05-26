package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemControllerTest {
    private ItemController itemController;
    private final ItemRepository itemRepository = mock(ItemRepository.class);
    @Before
    public void setup(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController,"itemRepository",itemRepository);
    }
    @Test
    public void getAllItems(){
        when(itemRepository.findAll()).thenReturn(getItems());
        final ResponseEntity<List<Item>> response = itemController.getItems();
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertArrayEquals(getItems().toArray(), response.getBody().toArray());
    }
    @Test
    public void getItemById(){
        when(itemRepository.findById(1L)).thenReturn(Optional.of(getItem1()));
        final ResponseEntity<Item> response = itemController.getItemById(1L);
        Assert.assertNotNull(response);
        Assert.assertEquals(200,response.getStatusCodeValue());
        Assert.assertEquals(response.getBody().getId(),getItem1().getId());
    }

    private List<Item> getItems(){
        Item item = new Item();
        item.setId(1L);
        item.setName("item 1");
        item.setDescription("test item");
        item.setPrice(BigDecimal.valueOf(10));
        Item item2 = new Item();
        item.setId(2L);
        item.setName("item 2");
        item.setDescription("test item2");
        item.setPrice(BigDecimal.valueOf(10));
        return Arrays.asList(item,item2);
    }
    private Item getItem1(){
        Item item = new Item();
        item.setId(1L);
        item.setName("item 1");
        item.setDescription("test item");
        item.setPrice(BigDecimal.valueOf(10));
        return item;
    }



}
