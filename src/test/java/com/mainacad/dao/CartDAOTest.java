package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.dao.model.CartSumDTO;
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
class CartDAOTest {
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
    void save() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());
    }

    @Test
    void getAllByUserAndPeriodTest() {
        User userOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        User userNotOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
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

        Cart cartOk = new Cart(Status.OPEN, userOk, timeOk);
        Cart cartNotOk1 = new Cart(Status.OPEN, userOk, timeNotOk);
        Cart cartNotOk2 = new Cart(Status.OPEN, userNotOk, timeOk);

        cartDAO.save(cartOk);
        cartDAO.save(cartNotOk1);
        cartDAO.save(cartNotOk2);
        carts.add(cartOk);
        carts.add(cartNotOk1);
        carts.add(cartNotOk2);
        assertNotNull(cartOk.getId());
        assertNotNull(cartNotOk1.getId());
        assertNotNull(cartNotOk2.getId());

        List<Cart> targetCarts = cartDAO.getAllByUserAndPeriod(userOk, periodFrom, periodTo);
        assertTrue(targetCarts.size() >= 1);

        boolean isInCollectionCartOk = false;
        boolean isInCollectionCartNotOk1 = false;
        boolean isInCollectionCartNotOk2 = false;

        for (Cart each : targetCarts) {
            if ((cartOk.getId()).equals(each.getId())) {
                isInCollectionCartOk = true;
            }
            if ((cartNotOk1.getId()).equals(each.getId())) {
                isInCollectionCartNotOk1 = true;
            }
            if ((cartNotOk2.getId()).equals(each.getId())) {
                isInCollectionCartNotOk2 = true;
            }
        }
        assertTrue(isInCollectionCartOk);
        assertTrue(!isInCollectionCartNotOk1);
        assertTrue(!isInCollectionCartNotOk2);
    }

    @Test
    void getByIdTest() {
        User user = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Cart savedCart = cartDAO.getById(cart.getId());
        assertNotNull(savedCart);
        assertNotNull(savedCart.getUser());
        assertNotNull(savedCart.getUser().getId());
    }

    @Test
    void getByUserAndOpenStatusTest() {
        User userOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        User userNotOk = new User("login2", "pass2", "first_name2", "last_name2", "email2", "phone2");
        userDAO.save(userOk);
        userDAO.save(userNotOk);
        users.add(userOk);
        users.add(userNotOk);
        assertNotNull(userOk.getId());
        assertNotNull(userNotOk.getId());

        Cart cartOk = new Cart(Status.OPEN, userOk, CURRENT_TIME);
        Cart cartNotOk1 = new Cart(Status.CLOSED, userOk, CURRENT_TIME);
        Cart cartNotOk2 = new Cart(Status.OPEN, userNotOk, CURRENT_TIME);

        cartDAO.save(cartOk);
        cartDAO.save(cartNotOk1);
        cartDAO.save(cartNotOk2);
        carts.add(cartOk);
        carts.add(cartNotOk1);
        carts.add(cartNotOk2);
        assertNotNull(cartOk.getId());
        assertNotNull(cartNotOk1.getId());
        assertNotNull(cartNotOk2.getId());

        Cart targetCart = cartDAO.getByUserAndOpenStatus(userOk);
        assertNotNull(targetCart);
        assertNotNull(targetCart.getUser());
        assertNotNull(targetCart.getUser().getId());
        assertEquals(targetCart.getUser().getId(), userOk.getId());
        assertEquals(targetCart.getId(), cartOk.getId());
    }

    @Test
    void updateStatusTest() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        cart.setStatus(Status.CLOSED);

        Cart targetCart = cartDAO.updateStatus(cart, Status.CLOSED);
        assertNotNull(targetCart);
        assertEquals(Status.CLOSED, targetCart.getStatus());
    }

    @Test
    void getAndDeleteTest() {
        User user = new User("login0", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        Cart cart = new Cart(Status.OPEN, user, CURRENT_TIME);
        cartDAO.save(cart);
        carts.add(cart);
        assertNotNull(cart.getId());

        Cart targetCart = cartDAO.getById(cart.getId());
        assertNotNull(targetCart);

        cartDAO.delete(cart);

        Cart deletedCart = cartDAO.getById(targetCart.getId());
        assertNull(deletedCart);
    }

    @Test
    void getItemsSumGroupedByUserTest() {
        Item item1 = new Item("name_1", "code_1", 30, 300);
        Item item2 = new Item("name_2", "code_2", 40, 0);
        Item item3 = new Item("name_2", "code_3", 50, 0);
        items.add(item1);
        items.add(item2);
        items.add(item3);
        itemDAO.save(item1);
        itemDAO.save(item2);
        itemDAO.save(item3);
        assertNotNull(item1.getId());
        assertNotNull(item2.getId());
        assertNotNull(item3.getId());

        User user1 = new User("login_1", "pass0", "first_name0", "last_name0", "email0", "phone0");
        User user2 = new User("login_2", "pass0", "first_name0", "last_name0", "email0", "phone0");
        userDAO.save(user1);
        userDAO.save(user2);
        users.add(user1);
        users.add(user1);
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());

        Long periodFrom = 12345678L;
        Long periodTo = 22345678L;
        Long timeOk1 = 12345679L;
        Long timeOk2 = 12345688L;
        Long timeNotOk = 1234L;

        Cart cart1 = new Cart(Status.CLOSED, user1, timeOk1);
        Cart cart2 = new Cart(Status.CLOSED, user2, timeNotOk);
        Cart cart3 = new Cart(Status.OPEN, user1, timeOk1);
        Cart cart4 = new Cart(Status.CLOSED, user2, timeOk1);
        Cart cart5 = new Cart(Status.CLOSED, user2, timeOk2);
        cartDAO.save(cart1);
        cartDAO.save(cart2);
        cartDAO.save(cart3);
        cartDAO.save(cart4);
        cartDAO.save(cart5);
        carts.add(cart1);
        carts.add(cart2);
        carts.add(cart3);
        carts.add(cart4);
        carts.add(cart5);
        assertNotNull(cart1.getId());
        assertNotNull(cart2.getId());
        assertNotNull(cart3.getId());
        assertNotNull(cart4.getId());
        assertNotNull(cart5.getId());

        Order order1 = new Order(item1, cart1, 1);
        Order order2 = new Order(item2, cart1, 2);
        Order order3 = new Order(item1, cart2, 3);
        Order order4 = new Order(item2, cart2, 4);
        Order order5 = new Order(item1, cart3, 5);
        Order order6 = new Order(item2, cart4, 6);
        Order order7 = new Order(item1, cart5, 7);
        Order order8 = new Order(item2, cart5, 8);
        orderDAO.save(order1);
        orderDAO.save(order2);
        orderDAO.save(order3);
        orderDAO.save(order4);
        orderDAO.save(order5);
        orderDAO.save(order6);
        orderDAO.save(order7);
        orderDAO.save(order8);
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);
        orders.add(order6);
        orders.add(order7);
        orders.add(order8);
        assertNotNull(order1.getId());
        assertNotNull(order2.getId());
        assertNotNull(order3.getId());
        assertNotNull(order4.getId());
        assertNotNull(order5.getId());
        assertNotNull(order6.getId());
        assertNotNull(order7.getId());
        assertNotNull(order8.getId());

        List<CartSumDTO> cartDTOS = cartDAO.getItemsSumGroupedByUser(periodFrom, periodTo);
        assertTrue(cartDTOS.size() >= 2);

//        CartDTO cartDTO1 = new CartDTO("login_1", 83, CURRENT_TIME - 50);
//        CartDTO cartDTO2 = new CartDTO("login_2", 770, CURRENT_TIME - 60);

        boolean isInCollectionRecord1 = false;
        boolean isInCollectionRecord2 = false;

        for (CartSumDTO each : cartDTOS) {
            if (each.getUserLogin().equals("login_1")
                    && (each.getSumItems().equals(110))
                    && (each.getCreationTime().equals(12345679L))) {
                isInCollectionRecord1 = true;
            }
            if (each.getUserLogin().equals("login_2")
                    && (each.getSumItems().equals(770))
                    && (each.getCreationTime().equals(12345679L))) {
                isInCollectionRecord2 = true;
            }
        }

        assertTrue(isInCollectionRecord1);
        assertTrue(isInCollectionRecord2);
    }
}