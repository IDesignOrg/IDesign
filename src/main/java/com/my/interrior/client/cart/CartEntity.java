package com.my.interrior.client.cart;

import com.my.interrior.client.interrior.InterriorEntity;
import com.my.interrior.client.user.UserEntity;
import jakarta.persistence.*;

@Entity(name = "cart")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "c_no")
    private Long CNo;

    @ManyToOne
    @JoinColumn(name = "u_id")
    private UserEntity UId;

    @OneToOne
    @JoinColumn(name = "i_no")
    private InterriorEntity INo;

}
