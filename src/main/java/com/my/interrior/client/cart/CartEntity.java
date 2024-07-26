package com.my.interrior.client.cart;

import java.util.ArrayList;
import java.util.List;

import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "cart")
@Getter
@Setter
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_no")
    private Long CNo;
    
    @Column(name = "quantity")
    private int quantity;
    
    @OneToMany(mappedBy = "cartEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartOptionEntity> cartOptions;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "shop_no")
    private ShopEntity ShopEntity;

}
