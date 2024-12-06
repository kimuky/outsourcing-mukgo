package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    default Advertisement findByIdOrElseThrow(Long advertisementId) {
        return findById(advertisementId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADVERTISEMENT_NOT_FOUND));
    }

}
