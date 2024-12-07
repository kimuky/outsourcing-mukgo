package com.example.outsourcing.order.repository;

import com.example.outsourcing.entity.Orders;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.store = :store WHERE o.id = :id")
    int updateStore(@Param("store")Store store, @Param("id")Long id);

    @Modifying
    @Transactional
    @Query("UPDATE orders o SET o.totalPrice = :totalPrice WHERE o.id = :id")
    int updateTotalPrice(@Param("totalPrice") Integer totalPrice, @Param("id")Long id);

    @Query("SELECT o FROM orders o WHERE o.user = :user")
    List<Orders> findByUser(@Param("user") User user);

    @Query("SELECT o FROM orders o WHERE o.store.id = :storeId")
    List<Orders> findByStoreId(@Param("storeId") Long storeId);
}
