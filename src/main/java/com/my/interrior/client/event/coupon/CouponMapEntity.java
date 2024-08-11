package com.my.interrior.client.event.coupon;

import java.time.LocalDate;

import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "couponMap")
@Getter
@Setter
@ToString
public class CouponMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    //유저 불러옴
    @ManyToOne
    @JoinColumn(name = "u_no")
    private UserEntity userEntity;

    //쿠폰 불러옴
    @ManyToOne
    @JoinColumn(name = "coupon_no")
    private CouponEntity couponEntity;

    //사용
    @Column(nullable = false)
    private boolean used;

    //쿠폰을 받은날
    @Column(nullable = false)
    private LocalDate assignedDate;

    private LocalDate usedDate;


}
