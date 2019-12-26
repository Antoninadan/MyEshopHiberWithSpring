package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDAO extends BaseDAO<User> {
    @Autowired
    ConnectionFactory connectionFactory;

    public List<User> findAll() {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM users";
        List<User> result = session.createNativeQuery(sql).getResultList();

        session.close();
        return result;
    }


}
