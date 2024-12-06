package com.example.outsourcing.advertisement.service;

import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.advertisement.dto.RequestResponseDto;
import com.example.outsourcing.advertisement.repository.AdvertisementRepository;
import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.store.repository.StoreRepository;
import com.example.outsourcing.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdvertisementService {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    public RequestResponseDto RequestAdvertisement(Long storeId, Long userId,
                                                   AdvertisementRequestDto requestDto) {
        User findUser = userRepository.findUserByIdOrElseThrow(userId);
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        // 가게 사장님과 사장님을 비교하여 해당 가게의 사장님이 맞는지 확인
        if (!findStore.getUser().getId().equals(findUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        Advertisement advertisement = new Advertisement(findUser, findStore, requestDto);
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);

        return new RequestResponseDto(savedAdvertisement);
    }
}
