package com.example.outsourcing.order.controller;

import com.example.outsourcing.entity.User;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponseDto> postOrder(
            @RequestBody List<OrderRequestDto> orderRequestDtos,
            HttpSession session
    ) {

        User user = (User) session.getAttribute("user");

        OrderResponseDto orderResponseDto = orderService.postOrder(user, orderRequestDtos);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponseDto);
    }

    // 주문 단건 조회
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderId(@PathVariable Long orderId) {

        OrderResponseDto findOrder = orderService.getOrderId(orderId);

        return ResponseEntity.status(HttpStatus.OK).body(findOrder);
    }

    // 주문 전체 조회
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(HttpSession session) {

        User user = (User) session.getAttribute("user");

        List<OrderResponseDto> orders = orderService.getOrders(user);

        return  ResponseEntity.status(HttpStatus.OK).body(orders);
    }
}
