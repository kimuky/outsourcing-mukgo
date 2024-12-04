package com.example.outsourcing.entity;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@Embeddable
@EqualsAndHashCode
public class OrderMenuId {

    private Long ordersId;

    private Long menuId;

    public OrderMenuId(Long ordersId, Long menuId) {
        this.ordersId = ordersId;
        this.menuId = menuId;
    }

    public OrderMenuId() {
    }
}
