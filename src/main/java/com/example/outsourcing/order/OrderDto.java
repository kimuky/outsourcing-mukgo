package com.example.outsourcing.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {

    private Long menuId;

    private String menuName;

    private int count;
}
