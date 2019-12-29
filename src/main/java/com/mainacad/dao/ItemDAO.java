package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.Item;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemDAO extends BaseDAO<Item> {
    @Autowired
    ConnectionFactory connectionFactory;

    public List<Item> findAll() {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM users";
        List<Item> result = session.createNativeQuery(sql).getResultList();

        session.close();
        return result;
    }
}
