package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.User;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class UserDAO extends BaseDAO<User> {
    @Autowired
    ConnectionFactory connectionFactory;

    public List<User> getAll() {
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

        String sql = "SELECT * FROM users WHERE login = ? AND password = ?";


        ////////////
        session.

        try ( Connection connection = ConnectionToDB.getConnection();
              PreparedStatement preparedStatement =
                      connection.prepareStatement(sql)
        ) {
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                User user = new User (
                        resultSet.getInt("id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("e_mail"),
                        resultSet.getString("phone")
                );
                return user;
            }


        session.close();
        return null;
    }

}
