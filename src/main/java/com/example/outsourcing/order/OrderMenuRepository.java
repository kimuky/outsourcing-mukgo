package com.example.outsourcing.order;

import com.example.outsourcing.entity.OrderMenu;
import com.example.outsourcing.entity.OrderMenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, OrderMenuId> {

    @Query("SELECT order_menu o FROM order_menu WHERE o.ordersId = :orderId")
    List<OrderMenu> findAllByOrderId(@Param("orderId") Long orderId);
}
