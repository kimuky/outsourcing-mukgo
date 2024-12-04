package com.example.outsourcing.entity;

import com.example.outsourcing.status.OrderStep;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "orders")
public class Orders extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStep step;

    public Orders() {
    }
}
