package com.mainacad.dao;

import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ItemDAO extends BaseDAO<Item> {
    @Autowired
    ConnectionFactory connectionFactory;

    public List<Item> getAll() {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM users";
        List<Item> result = session.createNativeQuery(sql).addEntity(Item.class).getResultList();

        session.close();
        return result;
    }

    public static List<Item> getAllByCart(Cart cart) {
        String sql = "SELECT items.* FROM items " + "JOIN orders o ON o.item_id = i.id " + "JOIN carts c ON c.id = o.cart_id "
                + "WHERE c.id = ? ";
        List<Item> items = new ArrayList<>();
        try (Connection connection = ConnectionToDB.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);) {
            preparedStatement.setInt(1, cart.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Item item = getItemFromTable(resultSet);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
