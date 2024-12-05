package com.example.outsourcing.order.repository;

import com.example.outsourcing.entity.OrderMenu;
import com.example.outsourcing.entity.OrderMenuId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMenuRepository extends JpaRepository<OrderMenu, OrderMenuId> {

}