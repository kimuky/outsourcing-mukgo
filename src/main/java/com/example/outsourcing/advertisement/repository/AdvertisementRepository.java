package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    default Advertisement findByIdOrElseThrow(Long advertisementId) {
        return findById(advertisementId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADVERTISEMENT_NOT_FOUND));
    }

    Optional<Advertisement> findAdvertisementByStoreAndAdvertisementStatus(Store findStore, AdvertisementStatus advertisementStatus);

    Boolean existsAdvertisementByUserAndStoreAndAdvertisementStatus(User findUser, Store findStore, AdvertisementStatus advertisementStatus);
}
