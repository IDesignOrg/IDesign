package com.my.interrior.admin.coupon;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.user.UserEntity;

@Repository
public interface CouponMapRepository extends JpaRepository<CouponMapEntity, Long>{
   Optional<CouponMapEntity> findByuserEntityAndCouponEntity(UserEntity userEntity, CouponEntity couponEntity);
   List<CouponMapEntity> findByuserEntity(UserEntity userEntity);
   List<CouponMapEntity> findByuserEntity_UNo(Long userNo);
   CouponMapEntity findByCouponEntity_CouponNoAndUserEntity_UNo(Long couponNo, Long uNo);
}
