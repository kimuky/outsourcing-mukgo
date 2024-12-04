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
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    public Menu() {
    }
}
