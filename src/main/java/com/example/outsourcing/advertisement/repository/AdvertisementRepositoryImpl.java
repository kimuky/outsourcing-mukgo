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

// 쿼린 DSL 동적으로 쿼리를 작동하게 하기 위함
@Repository
@RequiredArgsConstructor
public class AdvertisementRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    // 광고 테이블의 모든 칼럼을 보여줌
    public List<AdvertisementResponseDto> getAdvertisementList(String status) {
        return jpaQueryFactory.select(
                        Projections.constructor(
                                AdvertisementResponseDto.class,
                                advertisement.id,
                                advertisement.user.id,
                                advertisement.store.id,
                                advertisement.price,
                                advertisement.rejectComment,
                                advertisement.contractMonth,
                                advertisement.createdAt,
                                advertisement.contractDate,
                                advertisement.updatedAt,
                                advertisement.advertisementStatus))
                .from(advertisement).where(statusEquals(status)).fetch();
    }

    // 상태 파라미터에 따른 동적으로 쿼리를 추가 혹은 제외
    private BooleanExpression statusEquals(String status) {
        if (status == null || !validationStatus(status)) {
            return null;
        }
        return advertisement.advertisementStatus.eq(AdvertisementStatus.valueOf(status));
    }

    // ENUM 처리를 위함 만약 ENUM 외의 값을 입력하면 모든 광고 리스트를 보여줌
    private boolean validationStatus(String status) {
        for (AdvertisementStatus adStatus : AdvertisementStatus.values()) {
            if (adStatus.name().equals(status)) {
                return true;
            }
        }
        return false;
    }
}
