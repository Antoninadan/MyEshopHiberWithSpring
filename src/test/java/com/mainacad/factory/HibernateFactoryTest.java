package com.mainacad.factory;

import com.mainacad.config.AppConfig;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringJUnitConfig(AppConfig.class)
@ActiveProfiles({"test"})
class HibernateFactoryTest {

    @Autowired
    ConnectionFactory connectionFactory;

    @Test
    void getSessionFactory() {
        SessionFactory sessionFactory = connectionFactory.getSessionFactory();
        assertNotNull(sessionFactory);

        Session session = sessionFactory.openSession();
        assertNotNull(session);
        session.close();
    }
}