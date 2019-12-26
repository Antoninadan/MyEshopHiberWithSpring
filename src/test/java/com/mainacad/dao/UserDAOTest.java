package com.mainacad.dao;

import com.mainacad.config.AppConfig;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.User;
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
@ActiveProfiles("test")
class UserDAOTest {

    @Autowired
    ConnectionFactory connectionFactory;

    @Autowired
    UserDAO userDAO;
    List<User> users;

    @BeforeEach
    void setUp() {
        users = new ArrayList<>();

        User user = new User("ignatenko2207", "123456", "Alex",
                "Ignatenko", "test@email.com", "0967933438");

        users.add(user);
    }

//    @AfterEach
//    void tearDown() {
//        users.forEach(it -> UserDAO.delete(it));
//    }

    @Test
    void saveAndGetAndDelete() {


        User savedUser = userDAO.save(users.get(0));
        assertNotNull(savedUser);
        assertNotNull(savedUser.getId());

        User retrievedUser = userDAO.findOne(savedUser.getId());
        assertNotNull(retrievedUser);
        assertEquals("ignatenko2207", retrievedUser.getLogin());

        userDAO.delete(retrievedUser);

        User deletedUser = userDAO.findOne(savedUser.getId());
        assertNull(deletedUser);
    }
}