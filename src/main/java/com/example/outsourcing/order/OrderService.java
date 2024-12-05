package com.example.outsourcing.order;

import com.example.outsourcing.entity.*;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.status.OrderStep;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderMenuRepository orderMenuRepository;
    private MenuRepository menuRepository;

    // 주문 생성
    public OrderResponseDto postOrder(User user, List<OrderRequestDto> orderRequestDtos) {

        Orders order = new Orders(user, OrderStep.ORDER_COMPLETED);
        Orders savedOrder = orderRepository.save(order);
        List<OrderDto> orderMenus = new ArrayList<>();

        int totalPrice = 0;

        // 리스트로 받은 주문들을 하나씩 OrderMenu에 저장
        for (OrderRequestDto orderRequestDto : orderRequestDtos) {
            // 메뉴 가져오기
            Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

            totalPrice += menu.getPrice() * orderRequestDto.getCount();

            OrderMenuId orderMenuId = new OrderMenuId(savedOrder.getId(), orderRequestDto.getMenuId());
            OrderMenu orderMenu = new OrderMenu(orderMenuId, savedOrder, menu, orderRequestDto.getCount());

            OrderMenu savedOrderMenu = orderMenuRepository.save(orderMenu);
            OrderDto orderDto = new OrderDto(savedOrderMenu.getMenuId().getId(), savedOrderMenu.getMenuId().getMenuName(), savedOrderMenu.getFoodCount());
            orderMenus.add(orderDto);
        }

        orderRepository.updateTotalPrice(totalPrice, user);

        return OrderResponseDto.builder()
                .id(savedOrder.getId())
                .order(orderMenus)
                .totalPrice(totalPrice)
                .createdAt(savedOrder.getCreatedAt())
                .build();

    }

//    // 주문 단건 조회
//    public OrderResponseDto getOrderId(Long orderId) {
//
//    }
//
//    // 주문 전체 조회
//    public List<OrderResponseDto> getOrders() {
//
//    }
}
