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
import lombok.ToString;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@ToString
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponNo;
    //이름
    @Column(nullable = false, length = 50)
    private String couponName;
    //
    @Column(nullable = false)
    private double couponValue;
    //할인 %
    @Column(nullable = false)
    private int couponDiscount;
    //몇번 받을수 있는지
    @Column(nullable = false)
    private int couponLimit;
    //비활성화
    @Column(nullable = false)
    private String couponState;
    //시작일
    @Column(nullable = false)
    private LocalDate couponStartAt;
    
    @Column(nullable = false)
    private LocalDate couponCreateAt;
    //끝나는날
    @Column(nullable = false)
    private LocalDate couponEndAt;

}
