package com.example.outsourcing.review.repository;

import com.example.outsourcing.entity.Review;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT r FROM review r WHERE r.ordersId.store = :store AND r.user != :user AND r.rating >= :minRating AND r.rating <= :maxRating ORDER BY r.createdAt DESC")
    List<Review> findByStore(
            @Param("store") Store store,
            @Param("user") User user,
            @Param("minRating") Integer minRating,
            @Param("maxRating") Integer maxRating
    );


}
