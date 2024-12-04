package com.example.outsourcing.entity;

import com.example.outsourcing.status.StoreStatus;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.LocalTime;

@Getter
@Entity(name = "store")
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer minimumAmount;

    @Column(nullable = false)
    private LocalTime openTime;

    @Column(nullable = false)
    private LocalTime closeTime;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    public Store() {
    }
}
