package com.example.outsourcing.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class OrderDto {

    private Long menuId;

    private String menuName;

    private Integer count;
}
