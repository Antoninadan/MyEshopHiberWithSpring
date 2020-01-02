package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.dao.model.ItemDTO;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
@ActiveProfiles("dev")
class ItemDAOTest {
    private static final Long CURRENT_TIME = new Date().getTime();

    @Autowired
    ItemDAO itemDAO;
    @Autowired
    OrderDAO orderDAO;
    @Autowired
    CartDAO cartDAO;
    @Autowired
    UserDAO userDAO;

    List<Item> items;
    List<User> users;
    List<Cart> carts;
    List<Order> orders;

    @BeforeEach
    void setUp() {
        carts = new ArrayList<>();
        users = new ArrayList<>();
        items = new ArrayList<>();
        orders = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        orders.forEach(it -> orderDAO.delete(it));
        carts.forEach(it -> cartDAO.delete(it));
        users.forEach(it -> userDAO.delete(it));
        items.forEach(it -> itemDAO.delete(it));
    }

    @Test
    void saveTest() {
        Item item = new Item("name_0", "code_0", 1, 10);
        items.add(item);

        itemDAO.save(item);
        assertNotNull(item.getId());
    }

    @Test
    void getAllTest() {
        Item item1 = new Item("name_1", "code_1", 10, 100);
        Item item2 = new Item("name_2", "code_2", 20, 200);
        items.add(item1);
        items.add(item2);
        itemDAO.save(item1);
        itemDAO.save(item2);

        List<Item> targetItems = itemDAO.getAll();
        assertTrue(targetItems.size() >= 2);

        int count = 0;
        for (Item each:targetItems){
            if ((item1.getId()).equals(each.getId())) {count++;}
            if ((item2.getId()).equals(each.getId())) {count++;}
        }
        assertEquals(2,count);
    }

    @Test
    void getAllAvailableTest() {
        Item item3 = new Item("name_3", "code_3", 30, 300);
        Item item4 = new Item("name_4", "code_4", 40, 0);
        items.add(item3);
        items.add(item4);
        itemDAO.save(item3);
        itemDAO.save(item4);

        List<Item> targetItems = itemDAO.getAllAvailable();
        assertTrue(targetItems.size() >= 1);

        boolean isInCollectionItem3 = false;
        boolean isInCollectionItem4 = false;
        for (Item each:targetItems){
            if ((item3.getId()).equals(each.getId())) {isInCollectionItem3 = true;}
            if ((item4.getId()).equals(each.getId())) {isInCollectionItem4 = true;}
        }
        assertTrue(isInCollectionItem3);
        assertTrue(!isInCollectionItem4);
    }

    @Test
    void getAllByUserAndPeriodTest() {
        Item item1 = new Item("name_1", "code_3", 30, 300);
        Item item2 = new Item("name_2", "code_4", 40, 0);
        Item item3 = new Item("name_2", "code_4", 40, 0);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        itemDAO.save(item1);
        itemDAO.save(item2);
        itemDAO.save(item3);
        assertNotNull(item1.getId());
        assertNotNull(item2.getId());
        assertNotNull(item3.getId());

        User userOk = new User("login_item1", "pass0", "first_name0", "last_name0", "email0", "phone0");
        User userNotOk = new User("login_item2", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(userOk);
        userDAO.save(userNotOk);
        users.add(userOk);
        users.add(userNotOk);
        assertNotNull(userOk.getId());
        assertNotNull(userNotOk.getId());

        Long periodFrom = CURRENT_TIME - 100;
        Long periodTo = CURRENT_TIME;
        Long timeOk = CURRENT_TIME - 50;
        Long timeNotOk = CURRENT_TIME - 200;

        Cart cartOk = new Cart(Status.CLOSED, userOk, timeOk);
        Cart cartNotOk1 = new Cart(Status.CLOSED, userNotOk, timeNotOk);
        Cart cartNotOk2 = new Cart(Status.OPEN, userOk, timeNotOk);
        Cart cartNotOk3 = new Cart(Status.CLOSED, userOk, timeNotOk);
        cartDAO.save(cartOk);
        cartDAO.save(cartNotOk1);
        cartDAO.save(cartNotOk2);
        cartDAO.save(cartNotOk3);
        carts.add(cartOk);
        carts.add(cartNotOk1);
        carts.add(cartNotOk2);
        carts.add(cartNotOk3);
        assertNotNull(cartOk.getId());
        assertNotNull(cartNotOk1.getId());
        assertNotNull(cartNotOk2.getId());
        assertNotNull(cartNotOk3.getId());

        Order orderOk1 = new Order(item1, cartOk, 50);
        Order orderOk2 = new Order(item2, cartOk, 50);
        Order orderNotOk1 = new Order(item1, cartNotOk1, 50);
        Order orderNotOk2 = new Order(item2, cartNotOk2, 50);
        Order orderNotOk3 = new Order(item3, cartNotOk2, 50);
        Order orderNotOk4 = new Order(item1, cartNotOk3, 50);
        orderDAO.save(orderOk1);
        orderDAO.save(orderOk2);
        orderDAO.save(orderNotOk1);
        orderDAO.save(orderNotOk2);
        orderDAO.save(orderNotOk3);
        orderDAO.save(orderNotOk4);
        orders.add(orderOk1);
        orders.add(orderOk2);
        orders.add(orderNotOk1);
        orders.add(orderNotOk2);
        orders.add(orderNotOk3);
        orders.add(orderNotOk4);
        assertNotNull(orderOk1.getId());
        assertNotNull(orderOk2.getId());
        assertNotNull(orderNotOk1.getId());
        assertNotNull(orderNotOk2.getId());
        assertNotNull(orderNotOk3.getId());
        assertNotNull(orderNotOk4.getId());

        List<ItemDTO> itemDTOS = itemDAO.getAllByUserAndPeriod(userOk, periodFrom, periodTo);
        assertTrue(itemDTOS.size() >= 2);

        boolean isInCollectionItem1 = false;
        boolean isInCollectionItem2 = false;
        boolean isInCollectionItem3 = false;
        for (ItemDTO each:itemDTOS){
            if ((item1.getId()).equals(each.getId())) {isInCollectionItem1 = true;}
            if ((item2.getId()).equals(each.getId())) {isInCollectionItem2 = true;}
            if ((item3.getId()).equals(each.getId())) {isInCollectionItem3 = true;}
        }
        assertTrue(isInCollectionItem1);
        assertTrue(isInCollectionItem2);
        assertTrue(!isInCollectionItem3);
    }

    @Test
    void getByIdTest() {
        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Item targetItem = itemDAO.getById(item.getId());
        assertNotNull(targetItem);
        assertAll("Should equal all fields",
                ()->assertEquals(targetItem.getName(), item.getName()),
                ()->assertEquals(targetItem.getCode(), item.getCode()),
                ()->assertEquals(targetItem.getPrice(), item.getPrice()),
                ()->assertEquals(targetItem.getAvailability(), item.getAvailability()));
    }

    @Test
    void updateTest() {
        Item item = new Item("name_6", "code_6", 60, 600);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        item.setName("name_new");

        Item targetItem = itemDAO.update(item);
        assertNotNull(targetItem);
        assertEquals("name_new", targetItem.getName());
    }

    @Test
    void getAndDeleteTest() {
        Item item = new Item("name_7", "code_7", 70, 700);
        itemDAO.save(item);
        assertNotNull(item.getId());

        Item targetItem = itemDAO.getById(item.getId());
        assertNotNull(targetItem);

        itemDAO.delete(item);

        Item deletedItem = itemDAO.getById(targetItem.getId());
        assertNull(deletedItem);
    }
}