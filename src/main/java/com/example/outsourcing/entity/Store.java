package com.example.outsourcing.entity;

import com.example.outsourcing.status.StoreStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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

    @Setter
    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @OneToMany(mappedBy = "store", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Menu> menus = new ArrayList<>();

    public Store(User user, String name, Integer minimumAmount, LocalTime openTime, LocalTime closeTime) {
        this.user = user;
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.status = StoreStatus.OPEN;
    }

    public void updateStore(String name, Integer minimumAmount, LocalTime openTime, LocalTime closeTime) {
        this.name = name;
        this.minimumAmount = minimumAmount;
        this.openTime = openTime;
        this.closeTime = closeTime;
    }

    public Store() {
    }
}
