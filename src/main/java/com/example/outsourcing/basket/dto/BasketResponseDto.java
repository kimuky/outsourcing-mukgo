package com.example.outsourcing.basket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BasketResponseDto {

    private Long menuId;
    private Long storeId;
    private Integer count;

}
