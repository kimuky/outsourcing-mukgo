package com.example.outsourcing.order.dto;

import com.example.outsourcing.status.OrderStep;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDto {

    private Long id;

    private Long storeId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<OrderDto> order;

    private int totalPrice;

    private OrderStep orderStep;

    private LocalDateTime createdAt;

}
