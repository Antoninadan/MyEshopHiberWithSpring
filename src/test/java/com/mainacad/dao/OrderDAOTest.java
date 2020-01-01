package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.dao.model.OrderDTO;
import com.mainacad.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
@ActiveProfiles("dev")
class OrderDAOTest {
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
        carts.forEach(it ->  cartDAO.delete(it));
        users.forEach(it -> userDAO.delete(it));
        items.forEach(it -> itemDAO.delete(it));
    }

    @Test
    void save() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order = new Order(item, cart, 50);
        orderDAO.save(order);
        orders.add(order);
        assertNotNull(order.getId());
    }

    @Test
    void getAllByCart() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        Cart cartNotOk = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        cartDAO.save(cartNotOk);
        carts.add(cart);
        carts.add(cartNotOk);
        assertNotNull(cart.getId());
        assertNotNull(cartNotOk.getId());

        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order1 = new Order(item, cart, 50);
        Order order2 = new Order(item, cart, 50);
        Order order3 = new Order(item, cartNotOk, 50);
        orderDAO.save(order1);
        orderDAO.save(order2);
        orderDAO.save(order3);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        assertNotNull(order1.getId());
        assertNotNull(order2.getId());
        assertNotNull(order3.getId());

        List<Order> targetOrders = orderDAO.getAllByCart(cart);
        assertTrue(targetOrders.size() >= 2);

        int count = 0;
        for (Order each:targetOrders){
            if ((order1.getId()).equals(each.getId())) {count++;}
            if ((order2.getId()).equals(each.getId())) {count++;}
            if ((order3.getId()).equals(each.getId())) {count++;}
        }
        assertEquals(2,count);

    }

    @Test
    void getById() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order = new Order(item, cart, 50);
        orderDAO.save(order);
        orders.add(order);
        assertNotNull(order.getId());

        Order targetOrder = orderDAO.getById(order.getId());
        assertNotNull(targetOrder);
        assertNotNull(targetOrder.getItem());
        assertNotNull(targetOrder.getItem().getId());
        assertNotNull(targetOrder.getCart());
        assertNotNull(targetOrder.getCart().getId());
    }

    @Test
    void getAndDelete() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order = new Order(item, cart, 50);
        orderDAO.save(order);
        assertNotNull(order.getId());

        Order targetOrder = orderDAO.getById(order.getId());
        assertNotNull(targetOrder);

        orderDAO.delete(order);

        Order deletedOrder = orderDAO.getById(targetOrder.getId());
        assertNull(deletedOrder);
    }


    @Test
    void updateAmount() {
        User user = new User("login", "pass", "first_name", "last_name", "email", "phone");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Item item = new Item("name", "code", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order = new Order(item, cart, 50);
        orderDAO.save(order);
        orders.add(order);
        assertNotNull(order.getId());

        Integer amount=100;
        order.setAmount(amount);
        orderDAO.updateAmount(order, amount);
        Order targetOrder = orderDAO.getById(order.getId());
        assertNotNull(targetOrder);

        assertEquals(100, targetOrder.getAmount());
    }

    @Test
    void getAllDTOByCard() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        Cart cartNotOk = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        cartDAO.save(cartNotOk);
        carts.add(cart);
        carts.add(cartNotOk);
        assertNotNull(cart.getId());
        assertNotNull(cartNotOk.getId());

        Item item = new Item("name_5", "code_5", 50, 500);
        itemDAO.save(item);
        items.add(item);
        assertNotNull(item.getId());

        Order order1 = new Order(item, cart, 50);
        Order order2 = new Order(item, cart, 50);
        Order order3 = new Order(item, cartNotOk, 50);
        orderDAO.save(order1);
        orderDAO.save(order2);
        orderDAO.save(order3);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        assertNotNull(order1.getId());
        assertNotNull(order2.getId());
        assertNotNull(order3.getId());

        List<OrderDTO> targetOrderDTOS = orderDAO.getAllDTOByCard(cart);
        assertTrue(targetOrderDTOS.size() >= 2);

        int count = 0;
        for (OrderDTO each:targetOrderDTOS){
            if ((order1.getId()).equals(each.getOrderId())) {count++;}
            if ((order2.getId()).equals(each.getOrderId())) {count++;}
            if ((order3.getId()).equals(each.getOrderId())) {count++;}
        }
        assertEquals(2,count);

    }
}