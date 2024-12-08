package com.example.outsourcing.order.repository;

import com.example.outsourcing.entity.OrderMenu;
import com.example.outsourcing.entity.OrderMenuId;
import com.example.outsourcing.order.dto.OrderDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface OrderMenuRepository extends JpaRepository<OrderMenu, OrderMenuId> {

    @Modifying
    @Query(value = "INSERT INTO order_menu(orders_Id, menu_id, food_count) VALUES (:orders_id, :menu_id, :food_count ) " , nativeQuery = true)
    void insertOrderMenu(@Param("orders_id") Long orders_id, @Param("menu_id") Long menu_id, @Param("food_count") Integer food_count);

    @Query(value = "SELECT new com.example.outsourcing.order.dto.OrderDto(om.menuId.id, om.menuId.menuName, om.foodCount) " +
            "FROM order_menu om where om.ordersId.id = :orders_id")
    List<OrderDto> getOrderMenus(@Param("orders_id") Long orders_id);


    @Query(value = "SELECT SUM(m.price * om.food_count) FROM order_menu om inner join menu m on om.menu_id = m.id WHERE om.orders_Id = :orders_id ", nativeQuery = true)
    Object findTotalPrice(@Param("orders_id") Long orders_id);

}
