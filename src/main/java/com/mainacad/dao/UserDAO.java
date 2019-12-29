package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.User;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public User getByLoginAndPassword(String login, String password) {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM users WHERE login = ?1 AND password = ?2";

        NativeQuery nativeQuery = session.createNativeQuery(sql).addEntity(User.class);
        nativeQuery.setParameter(1, login);
        nativeQuery.setParameter(2, password);

        List<User> users = nativeQuery.getResultList();
        User result = null;
        if(users.size() > 0){
            result = users.get(0);
        }

        session.close();
        return result;
    }
}
