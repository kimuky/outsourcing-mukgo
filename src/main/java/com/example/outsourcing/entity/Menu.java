package com.example.outsourcing.entity;

import com.example.outsourcing.status.MenuStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
    private String menuName;

    @Column(nullable = false)
    private Integer price;

    @Setter
    @Enumerated(EnumType.STRING)
    private MenuStatus status;

    public Menu() {
    }

    public Menu(Store store, String menuName, Integer price, MenuStatus status) {
        this.store = store;
        this.menuName = menuName;
        this.price = price;
        this.status = status;
    }

    public void updateMenu(String menuName, Integer price) {
        this.menuName = menuName;
        this.price = price;
    }
}
