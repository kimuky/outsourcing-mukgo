package com.example.outsourcing.entity;

import com.example.outsourcing.status.OrderStep;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity(name = "orders")
public class Orders extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "total_price")
    private Integer totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStep step;

    @OneToMany
    @JoinColumn(name = "order_menu_id")
    private List<OrderMenu> orderMenu = new ArrayList<>();

    public Orders() {
    }

    public Orders(User user, OrderStep step) {
        this.user = user;
        this.step = step;
    }
}
