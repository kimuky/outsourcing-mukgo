package com.example.outsourcing.order.dto;

import lombok.Getter;

@Getter
public class OrderRequestDto {

    private long menuId;

    private int count;

    public OrderRequestDto(long menuId, int count) {
        this.menuId = menuId;
        this.count = count;
    }
}
