package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.status.AdvertisementStatus;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.outsourcing.entity.QAdvertisement.advertisement;

@Repository
@RequiredArgsConstructor
public class AdvertisementRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    public List<AdvertisementResponseDto> getAdvertisementList(String status) {
        return jpaQueryFactory.select(
                Projections.constructor(
                        AdvertisementResponseDto.class,
                        advertisement.id,
                        advertisement.user.id,
                        advertisement.store.id,
                        advertisement.price,
                        advertisement.contractDate,
                        advertisement.createdAt,
                        advertisement.advertisementStatus))
                .from(advertisement).where(statusEquals(status)).fetch();
    }

    private BooleanExpression statusEquals(String status) {
        if (status == null || !validationStatus(status)) {
            return null;
        }
        return advertisement.advertisementStatus.eq(AdvertisementStatus.valueOf(status));
    }

    private boolean validationStatus(String status) {
        for (AdvertisementStatus adStatus : AdvertisementStatus.values()) {
            if (adStatus.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
