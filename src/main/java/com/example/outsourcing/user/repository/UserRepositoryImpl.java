package com.example.outsourcing.user.repository;

import com.example.outsourcing.admin.dto.AllStaticsResponseDto;
import com.example.outsourcing.admin.dto.DailyStaticsResponseDto;
import com.example.outsourcing.admin.dto.MonthlyStaticsResponseDto;
import com.example.outsourcing.admin.dto.StartEndDateTimeDto;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.outsourcing.entity.QOrders.orders;
import static com.example.outsourcing.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl {

    private final JPAQueryFactory jpaQueryFactory;

    /**
     * 동적 쿼리
     * nativeQuery example -> SELECT count(1), SUM(total_price), 2024.05.15
     * FROM orders WHERE {store_id = 1} AND DATE_fORMAT(createdAt, '%Y-%m-%d') = 2024.05.15;
     * storeId의 여부에 따라 생략 후 리턴
     * @param storeId 가게 id
     * @param date 원하는 날짜
     * @return 원하는 날짜의 주문 건수, 주문 총액, 해당 날짜
     */
    public DailyStaticsResponseDto getDailyStatics(Long storeId, LocalDate date) {
        // querydsl 날짜 포맷팅 YYYY-MM-DD 형식
        StringExpression formattedDate
                = Expressions.stringTemplate("FUNCTION('DATE_FORMAT', {0}, '%Y-%m-%d')", orders.createdAt);


        return jpaQueryFactory
                .select(Projections.constructor(DailyStaticsResponseDto.class,
                        orders.count(), orders.totalPrice.sum(), Expressions.constant(date)))
                .from(orders)
                .where(storeEquals(storeId), formattedDate.eq(String.valueOf(date)))
                .fetchOne();
    }

    private BooleanExpression storeEquals(Long storeId) {
        if (storeId == null) {
            return null;
        }
        return store.id.eq(storeId);
    }

    /**
     * nativeQuery example -> SELECT count(1), SUM(total_price), 2024.05.01, 2024.05.31
     * FROM orders WHERE createdAt BETWEEN 2024.05.01 AND 2024.05.31
     * GROUP BY store_id;
     * @param storeId 가게 id
     * @param year 원하는 연도
     * @param month 원하는 달
     * @return 가게 id, 주문 건수, 주문 총액, 해당 연도와 달의 시작일, 해당 연도와 달의 마지막일
     */
    public List<MonthlyStaticsResponseDto> getMonthlyStatics(Long storeId, Integer year, Integer month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        return jpaQueryFactory.select(
                        Projections.constructor(MonthlyStaticsResponseDto.class,
                                orders.store.id, orders.count(), orders.totalPrice.sum(),
                                Expressions.constant(startDate),
                                Expressions.constant(endDate)))
                .from(orders)
                .where(orders.createdAt.yearMonth().eq(toIntegerDate(year, month)), storeEquals(storeId))
                .groupBy(orders.store).fetch();
    }

    private Integer toIntegerDate(Integer year, Integer month) {
        return year * 100 + month;
    }

    /**
     * nativeQuery example -> SELECT count(1), SUM(total_price), 2024.05.21, 2024.05.31
     * FROM orders WHERE createdAt BETWEEN 2024.05.21 AND 2024.05.31
     * GROUP BY store_id;
     * @param dateDto 원하는 시작 날짜, 원하는 마지막 날짜를 담고있는 dto
     * @return 가게 id, 주문 건수, 주문 총액, 원하는 시작날짜, 원하는 마지막날짜
     */
    public List<AllStaticsResponseDto> getAllStatics(StartEndDateTimeDto dateDto) {
        return jpaQueryFactory.select(
                Projections.constructor(AllStaticsResponseDto.class,
                        orders.store.id, orders.count(), orders.totalPrice.sum(),
                        Expressions.constant(dateDto.getStartDate()),
                        Expressions.constant(dateDto.getEndDate())))
                .from(orders)
                .where(orders.createdAt.between(dateDto.getStartDate(), dateDto.getEndDate()))
                .groupBy(store.id).fetch();
    }
}
