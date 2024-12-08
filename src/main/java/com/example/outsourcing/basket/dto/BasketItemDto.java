package com.example.outsourcing.basket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BasketItemDto {
    private Long menuId;
    private Long storeId;
    private Integer count;
}
