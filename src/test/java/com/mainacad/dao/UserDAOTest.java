package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(AppConfig.class)
@ActiveProfiles("dev")
class UserDAOTest {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    UserDAO userDAO;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();
    }

    @AfterEach
    void tearDown() {
        users.forEach(it -> userDAO.delete(it));
    }

    @Test
    void save() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());
    }

    @Test
    void update() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        User savedUser = userDAO.save(user);
        users.add(savedUser);
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());
        assertEquals("testPass", savedUser.getPassword());

        user.setPassword("newPass");

        User updatedUser = userDAO.update(user);
        assertNotNull(updatedUser);
        assertEquals("newPass", updatedUser.getPassword());
    }

    @Test
    void getAndDelete() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        userDAO.save(user);

        assertNotNull(user.getId());

        User targetUser = userDAO.getById(user.getId());
        assertNotNull(targetUser);
        userDAO.delete(targetUser);
        targetUser = userDAO.getById(user.getId());
        assertNull(targetUser);
    }

    @Test
    void getAll() {
        User user1 = new User("testLogin22", "testPass", "testName", "testLastName", "testEmail", "123456789");
        User user2 = new User("testLogin23", "testPass", "testName", "testLastName", "testEmail", "123456789");
        userDAO.save(user1);
        userDAO.save(user2);
        users.add(user1);
        users.add(user2);
        assertNotNull(user1.getId());
        assertNotNull(user2.getId());

        List<User> targetUsers = userDAO.getAll();
        assertTrue(targetUsers.size() >= 2);

        int count = 0;
        for (User each:targetUsers){
            if ((user1.getId()).equals(each.getId())) {count++;}
            if ((user2.getId()).equals(each.getId())) {count++;}
        }
        assertEquals(2,count);
    }

    @Test
    void getById() {
        User user = new User("testLogin", "testPass", "testName", "testLastName", "testEmail", "123456789");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        User targetUser = userDAO.getById(user.getId());
        assertNotNull(targetUser);
        assertNotNull(targetUser.getId());
    }

    @Test
    void getByLoginAndPassword() {
        User user = new User("testLogin100", "testPass100", "testName", "testLastName", "testEmail", "123456789");
        userDAO.save(user);
        users.add(user);
        assertNotNull(user.getId());

        User targetUser = userDAO.getByLoginAndPassword(user.getLogin(), user.getPassword());
        assertNotNull(targetUser);
        assertNotNull(targetUser.getId());
        assertEquals(targetUser.getLogin(),user.getLogin());
    }

}