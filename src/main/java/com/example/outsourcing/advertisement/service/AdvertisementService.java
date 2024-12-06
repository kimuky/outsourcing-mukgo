package com.example.outsourcing.advertisement.service;

import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
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

    public AdvertisementResponseDto RequestAdvertisement(Long storeId, Long userId, AdvertisementRequestDto requestDto) {
        User findUser = userRepository.findUserByIdOrElseThrow(userId);
        Store findStore = storeRepository.findByStoreOrElseThrow(storeId);

        if (!findStore.getUser().getId().equals(findUser.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN_OWNER);
        }

        Advertisement advertisement = new Advertisement(findUser, findStore, requestDto);
        Advertisement savedAdvertisement = advertisementRepository.save(advertisement);

        return new AdvertisementResponseDto(savedAdvertisement);
    }
}
