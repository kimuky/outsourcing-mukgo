package com.example.outsourcing.store.repository;


import com.example.outsourcing.entity.Store;
import com.example.outsourcing.entity.User;
import com.example.outsourcing.error.errorcode.ErrorCode;
import com.example.outsourcing.error.exception.CustomException;
import com.example.outsourcing.status.StoreStatus;
import com.example.outsourcing.store.dto.StoreResponseDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    long countByUserAndStatusNot(User user, StoreStatus storeStatus);

    /**
     * 가게 이름 검색 쿼리
     * @param name
     * @return
     */
    @Query("SELECT new com.example.outsourcing.store.dto.StoreResponseDto( " +
            "s.id, s.name, s.minimumAmount, s.openTime, s.closeTime, s.status) " +
            "FROM store s " +
            "WHERE (LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND user.id != :user_id AND s.status = 'OPEN')" +
            "OR (LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND user.id = :user_id)")
    List<StoreResponseDto> findByStoreAndNotUser(@Param("name") String name , @Param("user_id") Long userId);

    /**
     * 고객이 오픈인 가게만 조회되는 쿼리
     * @param name
     * @return
     */
    @Query("SELECT new com.example.outsourcing.store.dto.StoreResponseDto( " +
            "s.id, s.name, s.minimumAmount, s.openTime, s.closeTime, s.status) " +
            "FROM store s " +
            "WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%')) AND s.status = 'OPEN'")
    List<StoreResponseDto> findByStoreAndUser(@Param("name") String name);

    /**
     * 본인 가게 조회 쿼리
     * @param userId
     * @return
     */
    @Query("SELECT new com.example.outsourcing.store.dto.StoreResponseDto( " +
            "s.id, s.name, s.minimumAmount, s.openTime, s.closeTime, s.status) " +
            "FROM store s " +
            "WHERE s.user.id = :userId")
    List<StoreResponseDto> findByMyStore(@Param("user_id") Long userId);


    default Store findByStoreOrElseThrow(Long storeId) {
        return findById(storeId).orElseThrow(
                () -> new CustomException(ErrorCode.STORE_NOT_FOUND)
        );
    }

}
