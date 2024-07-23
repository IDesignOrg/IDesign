package com.my.interrior.client.cart;

import com.my.interrior.client.interrior.InterriorEntity;
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
    
    @Column(name = "option_value")
    private String optionValue;
    
    @Column(name = "quantity")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "shop_no")
    private ShopEntity ShopEntity;

}
