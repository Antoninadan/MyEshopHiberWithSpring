package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(AppConfig.class)
@ActiveProfiles ("test")
class ItemDAOTest {

    @Autowired
      ItemDAO itemDAO;
    List<Item> items;

    @BeforeEach
    void setUp() {
      items = new ArrayList<>();

        Item item = new Item("item name", "itemCode", 12345, 5);

        items.add(item);
    }

    @AfterEach
    void tearDown() {
        items.forEach(it -> itemDAO.delete(it));
    }

    @Test
    void saveAndGet() {

        Item savedItem = itemDAO.save(items.get(0));
        assertNotNull(savedItem);
        assertNotNull(savedItem.getId());

        Item retrievedItem = itemDAO.findOne(savedItem.getId());
        assertNotNull(retrievedItem);
        assertEquals("itemCode", retrievedItem.getCode());

    }

}