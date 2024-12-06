package com.example.outsourcing.advertisement.repository;

import com.example.outsourcing.advertisement.dto.AdvertisementResponseDto;
import com.example.outsourcing.status.AdvertisementStatus;
import com.example.outsourcing.status.StoreStatus;
import com.example.outsourcing.store.dto.StoreResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.outsourcing.entity.QAdvertisement.advertisement;
import static com.example.outsourcing.entity.QStore.store;

// 쿼린 DSL 동적으로 쿼리를 작동하게 하기 위함
@Repository
@RequiredArgsConstructor
public class AdvertisementRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    // 광고 테이블의 모든 칼럼을 보여줌
    public List<AdvertisementResponseDto> getAdvertisementList(String status, Long storeId) {
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
                .from(advertisement).where(statusEquals(status), StoreEquals(storeId)).fetch();
    }

    // 상태 파라미터에 따른 동적으로 쿼리를 추가 혹은 제외
    private BooleanExpression statusEquals(String status) {
        if (status == null || !validationStatus(status)) {
            return null;
        }
        return advertisement.advertisementStatus.eq(AdvertisementStatus.valueOf(status));
    }

    // 가게 id 여부에 따른 동적 쿼리 수행
    private BooleanExpression StoreEquals(Long storeId) {
        if (storeId == null) {
            return null;
        }
        return advertisement.store.id.eq(storeId);
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

    /**
     * 가게와 광고를 leftjoin 하여 가게와 연결된 광고를 찾고, 가게 이름 검색에 따른 가게가 조회가 되는데
     * 정렬 순은 광고중 인가? (ADVERTISING 제외하곤 우선순위 같음)
     * -> 광고 금액이 큰가? -> 계약한 달이 긴가?
     * @param name 가게 이름
     * @return 가게 아이디, 가게 이름, 가게 최소 주문금액, 오픈시간, 마감 시간, 가게 오픈 상태
     */
    public List<StoreResponseDto> findByStoreNameAndAdvertisement(String name) {
        return jpaQueryFactory.select(
                Projections.constructor(
                        StoreResponseDto.class,
                        store.id,
                        store.name,
                        store.minimumAmount,
                        store.openTime,
                        store.closeTime,
                        store.status)
                )
                .from(store)
                .leftJoin(store.advertisements, advertisement)
                .where(store.name.like("%" + name + "%"), store.status.eq(StoreStatus.OPEN))
                .orderBy(priorityAdvertising.asc(), advertisement.price.desc(), advertisement.contractMonth.desc()).fetch();
    }

    // ADVERTISING 제외하곤 우선순위 동일
    NumberExpression<Integer> priorityAdvertising = new CaseBuilder()
            .when(advertisement.advertisementStatus.eq(AdvertisementStatus.ADVERTISING)).then(1)
            .otherwise(2);

}
