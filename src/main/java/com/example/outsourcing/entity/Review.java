package com.example.outsourcing.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "review")
public class Review extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders ordersId;

    @Column(nullable = false)
    private String comment;

    @Column(columnDefinition = "tinyint", nullable = false)
    private Integer rating;

    public Review() {
    }

    public Review(User user, Orders ordersId, String comment, Integer rating) {
        this.user = user;
        this.ordersId = ordersId;
        this.comment = comment;
        this.rating = rating;
    }

}
