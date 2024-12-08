package com.example.outsourcing.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "order_menu")
public class OrderMenu extends BaseEntity {

    @EmbeddedId
    private OrderMenuId id;

    @MapsId("ordersId")
    @ManyToOne
    @JoinColumn(name = "orders_Id", insertable = false, updatable = false)
    private Orders ordersId;

    @MapsId("menuId")
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menuId;

    @Column(nullable = false)
    private Integer foodCount;

    public OrderMenu() {
    }

    public OrderMenu(OrderMenuId id, Orders ordersId, Menu menuId, Integer foodCount) {
        this.id = id;
        this.ordersId = ordersId;
        this.menuId = menuId;
        this.foodCount = foodCount;
    }
}
