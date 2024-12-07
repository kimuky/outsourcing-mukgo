package com.example.outsourcing.store.service;

import com.example.outsourcing.advertisement.repository.AdvertisementRepositoryImpl;
import com.example.outsourcing.entity.*;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.menu.dto.MenuResponseDto;
import com.example.outsourcing.menu.repository.MenuRepository;
import com.example.outsourcing.order.dto.OrderDto;
import com.example.outsourcing.order.dto.OrderResponseDto;
import com.example.outsourcing.order.repository.OrderRepository;
import com.example.outsourcing.status.Authority;
import com.example.outsourcing.status.MenuStatus;
import com.example.outsourcing.status.OrderStep;
import com.example.outsourcing.status.StoreStatus;
import com.example.outsourcing.store.dto.StoreMenuResponseDto;
import com.example.outsourcing.store.dto.StoreRequestDto;
import com.example.outsourcing.store.dto.StoreResponseDto;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    public final UserRepository userRepository;
    private final AdvertisementRepositoryImpl advertisementRepositoryImpl;
    private final OrderRepository orderRepository;
    private final MenuRepository menuRepository;

    /**
     * 가게 생성
     *
     * @param requestDto
     * @param loginUser
     * @return
     */
    public StoreResponseDto createStore(StoreRequestDto requestDto, User loginUser) {

        //권한 검증
        if (!loginUser.getAuthority().equals(Authority.OWNER)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        //가게 수 확인
        long activeStoreCount = storeRepository.countByUserAndStatusNot(loginUser, StoreStatus.CLOSURE);
        if (activeStoreCount >= 3) {
            throw new CustomException(ErrorCode.NOT_CREATE_STORE);
        }

        //오픈 시간, 마감 시간
        if (requestDto.getOpenTime().isAfter(requestDto.getCloseTime())) {
            throw new CustomException(ErrorCode.OPEN_AND_CLOSE);
        }

        Store store = new Store(
                loginUser,
                requestDto.getName(),
                requestDto.getMinimumAmount(),
                requestDto.getOpenTime(),
                requestDto.getCloseTime()
        );
        Store savedStore = storeRepository.save(store);

        return StoreResponseDto.fromStore(savedStore);
    }

    /**
     * 가게 수정
     *
     * @param storeId
     * @param loginUserId
     * @param name
     * @param minimumAmount
     * @param openTime
     * @param closeTime
     * @return
     */
    public StoreResponseDto updateStore(Long storeId, Long loginUserId, String name, Integer minimumAmount, LocalTime openTime, LocalTime closeTime) {

        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        //가게 ID 확인
        if (findStore.getId() == null) {
            throw new CustomException(ErrorCode.STORE_NOT_FOUND);
        }

        //본인의 가게 검증
        if (!findStore.getUser().getId().equals(loginUserId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        if (name == null || minimumAmount == null || openTime == null || closeTime == null) {
            throw new CustomException(ErrorCode.BAD_REQUEST);
        }

        findStore.updateStore(name, minimumAmount, openTime, closeTime);

        Store updatedStore = storeRepository.save(findStore);

        return StoreResponseDto.fromStore(updatedStore);

    }

    //    /**
//     * 가게 단건 조회
//     * @param storeId
//     * @return
//     */
    public StoreMenuResponseDto findStore(Long storeId, User loginUser) {
        // 가게 Id 확인
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        if (findStore.getStatus() == StoreStatus.CLOSURE) {
            if (loginUser.getAuthority() != Authority.OWNER || !findStore.getUser().getId().equals(loginUser.getId())) {
                throw new CustomException(ErrorCode.STORE_NOT_FOUND);
            }
        }

        // 엔티티 -> Dto 변환
        List<MenuResponseDto> menuDtos = new ArrayList<>();
        for (Menu menu : findStore.getMenus()) {
            if (menu.getStatus() != MenuStatus.DELETED) {
                menuDtos.add(new MenuResponseDto(
                        menu.getId(),
                        menu.getMenuName(),
                        menu.getPrice(),
                        menu.getStatus()
                ));
            }
        }

        return StoreMenuResponseDto.fromStoreMenu(findStore, menuDtos);
    }


    /**
     * 가게 이름 검색 조회 + 광고 우선 순위
     *
     * @param name
     * @return
     */
    public List<StoreResponseDto> SearchStoreByName(String name, User loginUser) {

        if (loginUser.getAuthority() != Authority.USER) {
            return storeRepository.findByStoreAndNotUser(name, loginUser.getId());
        }

        // 광고 구현했을 때, 광고 우선순위 검색
        return advertisementRepositoryImpl.findByStoreNameAndAdvertisement(name);

//        return storeRepository.findByStoreAndUser(name); -> 광고 구현을 안했을 시, 기존 검색
    }

    /**
     * 가게 폐업 변경
     *
     * @param storeId
     * @param userId
     */
    @Transactional
    public void closeStore(Long storeId, Long userId) {
        //가게 조회
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        //사용자 검증
        if (!findStore.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        // 상태 토글
        if (findStore.getStatus() == StoreStatus.OPEN) {
            findStore.setStatus(StoreStatus.CLOSURE);
        } else {
            findStore.setStatus(StoreStatus.OPEN);
        }
    }

    /**
     * 본인 가게만 조회
     *
     * @param loginUser
     * @return
     */
    public List<StoreResponseDto> findMyStore(User loginUser) {

        // 본인 가게 조회
        return storeRepository.findByMyStore(loginUser.getId());
    }


    /**
     * 사장 본인 가게 주문 조회
     * @param storeId
     * @param loginUser
     * @return
     */
    public List<OrderResponseDto> checkOrderStore(Long storeId, User loginUser) {
        //가게 조회
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        //사용자 검증
        if (!findStore.getUser().getId().equals(loginUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        // 가게에 들어온 주문 목록 조회
        List<Orders> orders = orderRepository.findByStoreId(storeId);

        List<OrderResponseDto> orderResponseDtos = new ArrayList<>();

        for (Orders order : orders) {
            // 주문 메뉴 조회
            List<OrderMenu> orderMenus = order.getOrderMenu();
            List<OrderDto> orderMenuDtos = new ArrayList<>();

            for (OrderMenu orderMenu : orderMenus) {
                String menuName = menuRepository.findById(orderMenu.getMenuId().getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.MENU_NOT_FOUND))
                        .getMenuName();
                Integer foodCount = orderMenu.getFoodCount();

                orderMenuDtos.add(new OrderDto(orderMenu.getMenuId().getId(), menuName, foodCount));
            }

            // 주문 정보를 OrderResponseDto로 변환
            OrderResponseDto orderResponseDto = OrderResponseDto.builder()
                    .id(order.getId())
                    .storeId(storeId)
                    .order(orderMenuDtos)
                    .totalPrice(order.getTotalPrice())
                    .orderStep(order.getStep())
                    .createdAt(order.getCreatedAt())
                    .build();

            orderResponseDtos.add(orderResponseDto);
        }

        return orderResponseDtos;
    }


    /**
     * 주문 상태 변경
     * @param storeId
     * @param orderId
     * @param loginUser
     * @return
     */
    @Transactional
    public OrderResponseDto updateOrderStatus(Long storeId, Long orderId, User loginUser) {
        // 가게 소유자인지 검증
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(ErrorCode.STORE_NOT_FOUND));

        if (!store.getUser().getId().equals(loginUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_PERMISSION);
        }

        // 주문 조회
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));

        // 주문이 해당 가게에 속하는지 확인
        if (!order.getStore().getId().equals(storeId)) {
            throw new CustomException(ErrorCode.ORDER_NOT_FOUND);
        }

        // 주문 상태 업데이트
        OrderStep currentStep = order.getStep();
        OrderStep nextStep = getNextOrderStep(currentStep);

        order.setStep(nextStep);
        orderRepository.save(order);

        // OrderResponseDto 생성 및 반환
        return OrderResponseDto.builder()
                .id(order.getId())
                .storeId(store.getId())
                .orderStep(order.getStep())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .build();
    }


    /**
     * 순차적 상태 변화
     * @param currentStep
     * @return
     */
    private OrderStep getNextOrderStep(OrderStep currentStep) {
        switch (currentStep) {
            case ORDER_COMPLETED:
                return OrderStep.ORDER_ACCEPTED;
            case ORDER_ACCEPTED:
                return OrderStep.COOKING;
            case COOKING:
                return OrderStep.COOKING_COMPLETED;
            case COOKING_COMPLETED:
                return OrderStep.DELIVERING;
            case DELIVERING:
                return OrderStep.DELIVERING_COMPLETED;
            default:
                throw new CustomException(ErrorCode.INVALID_ORDER_STEP);
        }
    }

}
