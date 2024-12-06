package com.example.outsourcing.entity;


import com.example.outsourcing.advertisement.dto.AdvertisementRequestDto;
import com.example.outsourcing.status.AdvertisementStatus;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity(name = "advertisement")
public class Advertisement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private Integer price;

    private String rejectComment;

    private Integer contractMonth;

    private LocalDateTime contractDate;

    @Enumerated(EnumType.STRING)
    private AdvertisementStatus advertisementStatus;

    public Advertisement(User findUser, Store findStore, AdvertisementRequestDto requestDto) {
        this.user = findUser;
        this.store = findStore;
        this.price = requestDto.getPrice();
        this.contractMonth = requestDto.getMonth();
        this.advertisementStatus = AdvertisementStatus.REQUEST;
    }

    public Advertisement() {

    }

    public void approveAdvertisement(LocalDateTime contractDate) {
        this.advertisementStatus = AdvertisementStatus.ADVERTISING;
        this.contractDate = contractDate;
    }

    public void rejectAdvertisement(String rejectComment) {
        this.advertisementStatus = AdvertisementStatus.REJECT;
        this.rejectComment = rejectComment;
    }
}
