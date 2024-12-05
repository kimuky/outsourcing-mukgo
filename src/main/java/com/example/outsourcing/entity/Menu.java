package com.example.outsourcing.entity;

import com.example.outsourcing.status.MenuStatus;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity(name = "menu")
public class Menu extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(nullable = false)
    private String muneName;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    public Menu() {
    }

    public Menu(Store store, String manuName, Integer price, MenuStatus status) {
        this.store = store;
        this.muneName = manuName;
        this.price = price;
        this.status = status;

    }
}
