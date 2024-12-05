package com.example.outsourcing.review.repository;

import com.example.outsourcing.entity.Review;
import com.example.outsourcing.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM review r WHERE r.ordersId.store = :store")
    List<Review> findByStore(@Param("store") Store store);
}
