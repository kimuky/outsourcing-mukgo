package com.example.outsourcing.admin.service;

import com.example.outsourcing.admin.dto.BetweenStaticsResponseDto;
import com.example.outsourcing.admin.dto.DailyStaticsResponseDto;
import com.example.outsourcing.admin.dto.MonthlyStaticsResponseDto;
import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.advertisement.dto.ApproveResponseDto;
import com.example.outsourcing.advertisement.dto.RejectResponseDto;
import com.example.outsourcing.advertisement.repository.AdvertisementRepository;
import com.example.outsourcing.advertisement.repository.AdvertisementRepositoryImpl;
import com.example.outsourcing.entity.Advertisement;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.AdvertisementStatus;
import com.example.outsourcing.user.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepositoryImpl userRepositoryImpl;
    private final AdvertisementRepository advertisementRepository;
    private final AdvertisementRepositoryImpl advertisementRepositoryImpl;

    public DailyStaticsResponseDto getDailyStatics(Long storeId, LocalDate date) {
        return userRepositoryImpl.getDailyStatics(storeId, date);
    }

    public List<MonthlyStaticsResponseDto> getMonthlyStatics(Long storeId, Integer year, Integer month) {
        return userRepositoryImpl.getMonthlyStatics(storeId, year, month);
    }

    public List<BetweenStaticsResponseDto> getStaticsBetween(Long storeId, StartEndDateTimeDto dateDto) {
        return userRepositoryImpl.getStaticsBetween(storeId, dateDto);
    }

    public List<AdvertisementResponseDto> getAdvertisementList(String status, Long storeId) {
        return advertisementRepositoryImpl.getAdvertisementList(status, storeId);
    }

    @Transactional
    public ApproveResponseDto approveAdvertisement(Long advertisementId) {
        Advertisement findAdvertisement
                = advertisementRepository.findByIdOrElseThrow(advertisementId);

        // 광고 상태가 요청이 아니라면 예외 처리
        if (!findAdvertisement.getAdvertisementStatus().equals(AdvertisementStatus.REQUEST)) {
            throw new CustomException(ErrorCode.FORBIDDEN_APPROVE_ADVERTISEMENT);
        }

        // 승인 시, 요청했던 달을 더해서 광고 기간 설정
        LocalDateTime contractDate = LocalDateTime.now().plusMonths(findAdvertisement.getContractMonth());

        // 광고 승인, 상태 및 광고 기간 업데이트
        findAdvertisement.approveAdvertisement(contractDate);

        return new ApproveResponseDto(findAdvertisement);
    }

    @Transactional
    public RejectResponseDto rejectAdvertisement(Long advertisementId, String rejectComment) {
        Advertisement findAdvertisement
                = advertisementRepository.findByIdOrElseThrow(advertisementId);

        // 광고 상태가 요청이 아니라면 예외 처리
        if (!findAdvertisement.getAdvertisementStatus().equals(AdvertisementStatus.REQUEST)) {
            throw new CustomException(ErrorCode.FORBIDDEN_REJECT_ADVERTISEMENT);
        }

        // 광고 상태 REJECT 변경
        findAdvertisement.rejectAdvertisement(rejectComment);

        return new RejectResponseDto(findAdvertisement);
    }
}
