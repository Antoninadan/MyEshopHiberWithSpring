package com.mainacad.factory;

import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.Order;
import com.mainacad.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;


public class H2Factory implements ConnectionFactory{

    private SessionFactory sessionFactory;

    public SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
//                // for 1 variant with hibernate.cfg.xml
//                Configuration configuration = new Configuration().configure();
//                sessionFactory = configuration.buildSessionFactory();

                Configuration configuration = new Configuration();

                Properties properties = new Properties();

                properties.setProperty("hibernate.connection.driverClassName", "org.h2.Driver");
                properties.setProperty("hibernate.connection.url", "jdbc:h2:~/hiber_spring");
                properties.setProperty("hibernate.connection.username", "tonya");
                properties.setProperty("hibernate.connection.password", "estafeta");
                properties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
                properties.setProperty("hibernate.hbm2ddl.auto", "update");
                properties.setProperty("hibernate.show_sql", "true");
                properties.setProperty("hibernate.format_sql", "true");

                configuration.addProperties(properties);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                configuration.addAnnotatedClass(Item.class);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Order.class);
                configuration.addAnnotatedClass(Cart.class);

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}
