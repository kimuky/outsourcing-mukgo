package com.example.outsourcing.basket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BasketResponseDto {

    private Long menuId;
    private Long storeId;
    private Integer count;


    public static BasketResponseDto fromBasket(BasketItemDto item) {
        return new BasketResponseDto(
                item.getMenuId(),
                item.getStoreId(),
                item.getCount()
        );
    }
}
