package com.example.outsourcing.store.service;

import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.Authority;
import com.example.outsourcing.status.StoreStatus;
import com.example.outsourcing.store.dto.StoreRequestDto;
import com.example.outsourcing.store.dto.StoreResponseDto;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    public final UserRepository userRepository;

    /**
     * 가게 생성
     * @param requestDto
     * @param loginUser
     * @return
     */
    public StoreResponseDto createStore(StoreRequestDto requestDto, User loginUser) {

        //권한 검증
        if(!loginUser.getAuthority().equals(Authority.OWNER)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        //가게 수 확인
        long activeStoreCount = storeRepository.countByUserAndStatusNot(loginUser, StoreStatus.CLOSURE);
        if(activeStoreCount >= 3) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        //오픈 시간, 마감 시간
        if(requestDto.getOpenTime().isAfter(requestDto.getCloseTime())) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
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
     * @param storeId
     * @param loginUserId
     * @param name
     * @param minimumAmount
     * @param openTime
     * @param closeTime
     * @return
     */
    public StoreResponseDto updateStore(Long storeId, Long loginUserId, String name, Integer minimumAmount, LocalTime openTime, LocalTime closeTime) {

        Store findStore = storeRepository.findByOrElseThrow(storeId);
        Long storeUserId = findStore.getUser().getId();

        User findUser = userRepository.findById(loginUserId).orElseThrow(
                () -> new CustomException(ErrorCode.NOT_FOUND_USER)
        );
        //본인의 가게 검증
        if(!findStore.getUser().getId().equals(loginUserId)) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }
        //가게 ID 확인
        if(findStore.getId() == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_STORE);
        }

        if(name == null || minimumAmount == null || openTime == null || closeTime == null) {
            throw new CustomException(ErrorCode.NOT_FOUND_USER);
        }

        findStore.updateStore(name, minimumAmount, openTime, closeTime);

        Store updatedStore = storeRepository.save(findStore);

        return StoreResponseDto.fromStore(updatedStore);

    }

    /**
     * 가게 단건 조회
     * @param storeId
     * @return
     */
    public StoreResponseDto findStore(Long storeId) {
        // 가게 Id 확인
        Store findStore = storeRepository.findByOrElseThrow(storeId);

        return StoreResponseDto.fromStore(findStore);
    }
}
