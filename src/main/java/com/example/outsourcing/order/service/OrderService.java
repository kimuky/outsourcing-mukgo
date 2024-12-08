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
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {
    private OrderRepository orderRepository;
    private OrderMenuRepository orderMenuRepository;
    private MenuRepository menuRepository;

    // 주문 생성
    @Transactional
    public OrderResponseDto postOrder(User user, List<OrderRequestDto> orderRequestDtos) {

        LocalTime now = LocalTime.now();
        Orders order = new Orders(user, OrderStep.ORDER_COMPLETED);
        Orders savedOrder = orderRepository.save(order);
        List<OrderDto> orderMenus = new ArrayList<>();

        int totalPrice = 0;
        Store orderStore = menuRepository.findById(orderRequestDtos.get(0).getMenuId())
                .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND)).getStore();

        // 리스트로 받은 주문들을 하나씩 OrderMenu에 저장
        for (OrderRequestDto orderRequestDto : orderRequestDtos) {
            // 메뉴 가져오기
            Menu menu = menuRepository.findById(orderRequestDto.getMenuId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND));

            Store store = menu.getStore();

            // 오픈시간이 아닐때 예외
            if (now.isBefore(store.getOpenTime()) || now.isAfter(store.getCloseTime())) {
                throw new CustomException(ErrorCode.NOT_OPEN_TIME);
            }

            if (!store.getId().equals(orderStore.getId())) {
                throw new CustomException(ErrorCode.STORE_NOT_FOUND);
            }

            totalPrice += menu.getPrice() * orderRequestDto.getCount();

            OrderMenuId orderMenuId = new OrderMenuId(savedOrder.getId(), orderRequestDto.getMenuId());
            OrderMenu orderMenu = new OrderMenu(orderMenuId, savedOrder, menu, orderRequestDto.getCount());
            OrderMenu savedOrderMenu = orderMenuRepository.save(orderMenu);

            OrderDto orderDto = new OrderDto(savedOrderMenu.getMenuId().getId(), savedOrderMenu.getMenuId().getMenuName(), savedOrderMenu.getFoodCount());
            orderMenus.add(orderDto);
        }

        // 주문 최소 금액 안 맞출 때 예외
        if (totalPrice < orderStore.getMinimumAmount()) {
            throw new CustomException(ErrorCode.UNDER_MINIMUM_AMOUNT);
        }

        orderRepository.updateStore(orderStore, savedOrder.getId());
        orderRepository.updateTotalPrice(totalPrice, savedOrder.getId());

        return OrderResponseDto.builder()
                .id(savedOrder.getId())
                .storeId(orderStore.getId())
                .order(orderMenus)
                .totalPrice(totalPrice)
                .orderStep(OrderStep.ORDER_COMPLETED)
                .createdAt(savedOrder.getCreatedAt())
                .build();

    }

    // 주문 단건 조회
    public OrderResponseDto getOrderId(Long orderId) {
        // 주문 Id 확인
        Orders findOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        List<OrderMenu> orderMenus = findOrder.getOrderMenu();
        List<OrderDto> orderMenuDtos = new ArrayList<>();

        for (OrderMenu orderMenu : orderMenus) {
            String menuName = menuRepository.findById(orderId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND))
                    .getMenuName();
            Integer foodCount = orderMenuRepository.findById(new OrderMenuId(orderId, orderMenu.getMenuId().getId()))
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND))
                    .getFoodCount();
            orderMenuDtos.add(new OrderDto(orderMenu.getMenuId().getId(), menuName, foodCount));
        }

        return OrderResponseDto.builder()
                .id(findOrder.getId())
                .storeId(findOrder.getStore().getId())
                .order(orderMenuDtos)
                .totalPrice(findOrder.getTotalPrice())
                .orderStep(OrderStep.ORDER_COMPLETED)
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
                        .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND))
                        .getMenuName();
                Integer foodCount = orderMenuRepository.findById(new OrderMenuId(orderMenu.getOrdersId().getId(), orderMenu.getMenuId().getId()))
                        .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND))
                        .getFoodCount();
                orderMenuDtos.add(new OrderDto(orderMenu.getMenuId().getId(), menuName, foodCount));
            }

            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .id(order.getId())
                    .storeId(order.getStore().getId())
                    .order(orderMenuDtos)
                    .totalPrice(order.getTotalPrice())
                    .orderStep(OrderStep.ORDER_COMPLETED)
                    .createdAt(order.getCreatedAt())
                    .build();
            orderResponseDtos.add(orderResponseDto);
        }

        return orderResponseDtos;
    }
}
