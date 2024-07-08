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

@Entity
@Table(name = "couponMap")
@Getter
@Setter
public class CouponMapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "u_no")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "couponId")
    private CouponEntity couponEntity;

    @Column(nullable = false)
    private boolean used;

    @Column(nullable = false)
    private LocalDate assignedDate;

    private LocalDate usedDate;


}
