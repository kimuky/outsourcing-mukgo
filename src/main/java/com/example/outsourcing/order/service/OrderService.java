package com.example.outsourcing.order.service;

import com.example.outsourcing.entity.*;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.order.dto.OrderDto;
import com.example.outsourcing.order.dto.OrderRequestDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.repository.OrderMenuRepository;
import com.example.outsourcing.order.repository.OrderRepository;
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
        List<Store> stores = new ArrayList<>();

        // 리스트로 받은 주문들을 하나씩 OrderMenu에 저장
        for (OrderRequestDto orderRequestDto : orderRequestDtos) {
            // 메뉴 가져오기
            Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU));

            Store store = menu.getStore();
            stores.add(store);

            totalPrice += menu.getPrice() * orderRequestDto.getCount();

            OrderMenuId orderMenuId = new OrderMenuId(savedOrder.getId(), orderRequestDto.getMenuId());
            OrderMenu orderMenu = new OrderMenu(orderMenuId, savedOrder, menu, orderRequestDto.getCount());

            OrderMenu savedOrderMenu = orderMenuRepository.save(orderMenu);
            OrderDto orderDto = new OrderDto(savedOrderMenu.getMenuId().getId(), savedOrderMenu.getMenuId().getMenuName(), savedOrderMenu.getFoodCount());
            orderMenus.add(orderDto);
        }

        orderRepository.updateStore(stores.get(0), savedOrder.getId());
        orderRepository.updateTotalPrice(totalPrice, savedOrder.getId());

        return OrderResponseDto.builder()
                .id(savedOrder.getId())
                .storeId(stores.get(0).getId())
                .order(orderMenus)
                .totalPrice(totalPrice)
                .createdAt(savedOrder.getCreatedAt())
                .build();

    }

    // 주문 단건 조회
    public OrderResponseDto getOrderId(Long orderId) {
        // 주문 Id 확인
        Orders findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER));

        List<OrderMenu> orderMenus = findOrder.getOrderMenu();
        List<OrderDto> orderMenuDtos = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus) {
            String menuName = menuRepository.findById(orderId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU))
                    .getMenuName();
            Integer foodCount = orderMenuRepository.findById(new OrderMenuId(orderId, orderMenu.getMenuId().getId()))
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER))
                    .getFoodCount();
            orderMenuDtos.add(new OrderDto(orderMenu.getMenuId().getId(), menuName, foodCount));
        }

        return OrderResponseDto.builder()
                .id(findOrder.getId())
                .storeId(findOrder.getStore().getId())
                .order(orderMenuDtos)
                .totalPrice(findOrder.getTotalPrice())
                .createdAt(findOrder.getCreatedAt())
                .build();

    }

    // 주문 전체 조회
    public List<OrderResponseDto> getOrders(User user) {

        List<Orders> findOrders = orderRepository.findByUser(user);
        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (Orders order : findOrders) {

            List<OrderMenu> orderMenus = order.getOrderMenu();
            List<OrderDto> orderMenuDtos = new ArrayList<>();

            for (OrderMenu orderMenu : orderMenus) {
                String menuName = menuRepository.findById(orderMenu.getMenuId().getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_MENU))
                        .getMenuName();
                Integer foodCount = orderMenuRepository.findById(new OrderMenuId(orderMenu.getOrdersId().getId(), orderMenu.getMenuId().getId()))
                        .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ORDER))
                        .getFoodCount();
                orderMenuDtos.add(new OrderDto(orderMenu.getMenuId().getId(), menuName, foodCount));
            }

            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .id(order.getId())
                    .storeId(order.getStore().getId())
                    .order(orderMenuDtos)
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreatedAt())
                    .build();
            orderResponseDtos.add(orderResponseDto);
        }

        return orderResponseDtos;
    }
}
