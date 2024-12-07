package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.AdvertisementStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
    default Advertisement findByIdOrElseThrow(Long advertisementId) {
        return findById(advertisementId)
                .orElseThrow(() -> new CustomException(ErrorCode.ADVERTISEMENT_NOT_FOUND));
    }

    Boolean existsAdvertisementByUserAndStoreAndAdvertisementStatus(User findUser, Store findStore, AdvertisementStatus advertisementStatus);


    @Modifying
    @Transactional
    @Query("UPDATE advertisement a SET a.advertisementStatus= 'END' " +
            "WHERE a.contractDate <= current_time AND a.advertisementStatus = 'ADVERTISING'")
    void ExpiredAdvertisements(@Param("current_time")LocalDateTime currentTime);
}
