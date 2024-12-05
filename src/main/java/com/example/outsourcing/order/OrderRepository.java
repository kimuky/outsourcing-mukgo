package com.example.outsourcing.order;

import com.example.outsourcing.entity.Orders;
import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.totalPrice = :totalPrice WHERE o.user = :user")
    int updateTotalPrice(@Param("totalPrice") Integer totalPrice, @Param("user")User user);
}
