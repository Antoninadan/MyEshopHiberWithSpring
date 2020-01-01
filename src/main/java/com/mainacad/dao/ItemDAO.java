package com.mainacad.dao;

import com.mainacad.dao.model.ItemDTO;
import com.mainacad.factory.ConnectionFactory;
import com.mainacad.model.Cart;
import com.mainacad.model.Item;
import com.mainacad.model.User;
import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO extends BaseDAO<Item> {
    @Autowired
    ConnectionFactory connectionFactory;

    public List<Item> getAll() {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM items";
        List<Item> result = session.createNativeQuery(sql).addEntity(Item.class).getResultList();

        session.close();
        return result;
    }

    public List<Item> getAllByCart(Cart cart) {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT items.* FROM items " + "JOIN orders o ON o.item_id = i.id " + "JOIN carts c ON c.id = o.cart_id "
                + "WHERE c.id = ? ";

        NativeQuery nativeQuery = session.createNativeQuery(sql).addEntity(Item.class);
        nativeQuery.setParameter(1, cart.getId());

        List<Item> result = nativeQuery.getResultList();

        session.close();
        return result;
    }

    public List<Item> getAllAvailable() {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT * FROM items WHERE availability > 0";
        List<Item> result = session.createNativeQuery(sql).addEntity(Item.class).getResultList();

        session.close();
        return result;
    }

    public List<ItemDTO> getAllByUserAndPeriod(User user, Long timeFrom, Long timeTo) {
        Session session = connectionFactory.getSessionFactory().openSession();
        session.getTransaction().begin();

        String sql = "SELECT i.id as itemid, "
                + "i.name as item_name, "
                + "i.price as item_price "
                + "FROM items i "
                + "JOIN orders o ON o.item_id = i.id "
                + "JOIN carts c ON c.id = o.cart_id "
                + "WHERE c.user_id = ? "
                + "AND c.creation_time >=?  AND c.creation_time <=? "
                + "AND c.status = 2";

        NativeQuery nativeQuery = session.createNativeQuery(sql).addEntity(Item.class);
        nativeQuery.setParameter(1, user.getId());
        nativeQuery.setParameter(2, timeFrom);
        nativeQuery.setParameter(3, timeTo);

        List<ItemDTO> result = nativeQuery.getResultList();

        session.close();
        return result;
    }
}
