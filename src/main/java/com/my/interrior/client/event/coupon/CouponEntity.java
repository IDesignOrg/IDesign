package com.my.interrior.client.event.coupon;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Coupon")
@Getter
@Setter
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId;

    @Column(nullable = false, length = 50)
    private String couponName;

    @Column(nullable = false)
    private double couponValue;

    @Column(nullable = false)
    private int couponDiscount;

    @Column(nullable = false)
    private int couponLimit;

    @Column(nullable = false)
    private String couponState;

    @Column(nullable = false)
    private LocalDate couponStartAt;

    @Column(nullable = false)
    private LocalDate couponCreateAt;

    @Column(nullable = false)
    private LocalDate couponEndAt;

}
