package com.mainacad.dao.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemDTORepository extends JpaRepository{
    @Query("SELECT com.mainacad.dao.model.ItemDTO(i.id, i.name, i.price) " +
            "FROM items i " +
            "JOIN orders o ON o.item_id = i.id " +
            "JOIN carts c ON c.id = o.cart_id " +
            "WHERE c.user_id = :user_id " +
            "AND c.creation_time >=:time_from  AND c.creation_time <=:time_to " +
            "AND c.status = 2")
    public List<ItemDTO> getAllByUserAndPeriod(@Param("user_id") Integer userId,
                                               @Param("time_from") Long timeFrom,
                                               @Param("time_to") Long timeTo);

    //    @Query("SELECT " +
//            "i.id as itemid, " +
//            "i.name as item_name, " +
//            "i.price as item_price " +
//            "FROM items i " +
//            "JOIN orders o ON o.item_id = i.id " +
//            "JOIN carts c ON c.id = o.cart_id " +
//            "WHERE c.user_id = :user_id " +
//            "AND c.creation_time >=:time_from  AND c.creation_time <=:time_to "
//            "AND c.status = 2")
//    public List<ItemDTO> getAllByUserAndPeriod(@Param("user_id") Integer userId,
//                                               @Param("time_from") Long timeFrom,
//                                               @Param("time_to") Long timeTo);
}
