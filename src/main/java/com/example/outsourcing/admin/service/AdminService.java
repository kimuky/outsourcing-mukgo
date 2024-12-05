package com.example.outsourcing.admin.service;

import com.example.outsourcing.admin.dto.AllStaticsResponseDto;
import com.example.outsourcing.admin.dto.DailyStaticsResponseDto;
import com.example.outsourcing.admin.dto.MonthlyStaticsResponseDto;
import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.example.outsourcing.user.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepositoryImpl userRepositoryImpl;

    public DailyStaticsResponseDto getDailyStatics(Long storeId, LocalDate date) {
        return userRepositoryImpl.getDailyStatics(storeId, date);
    }

    public List<MonthlyStaticsResponseDto> getMonthlyStatics(Long storeId, Integer year, Integer month) {
        return userRepositoryImpl.getMonthlyStatics(storeId, year, month);
    }

    public List<AllStaticsResponseDto> getStaticsAll(StartEndDateTimeDto dateDto) {
        return userRepositoryImpl.getAllStatics(dateDto);
    }
}
