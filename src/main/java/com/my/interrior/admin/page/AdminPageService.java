package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.notice.NoticeRepository;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.csc.recover.RecoveryRepository;
import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.ordered.OrderedEntity;
import com.my.interrior.client.ordered.OrderedRepository;
import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.shop.ShopRepository;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserService;

import jakarta.transaction.Transactional;

@Service
public class AdminPageService {
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	@Autowired
	private ShopReviewRepository shopReviewRepository;
	
	@Autowired
	private NoticeRepository noticerepository;
	
	@Autowired
	private RecoveryRepository recoveryRepository;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private CouponMapRepository couponMap;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private OrderedRepository orderedRepository;
	
	
	//유저 카운트
	public long getUserCount() {
        return userRepository.count();
    }
	//상점 카운트
	public long getShopCount() {
		return shopRepository.count();
	}
	//리뷰 카운트
	public long getReviewCount() {
		return reviewRepository.count();
	}
	//가장 높은 조회수를 가진 상점
	public Optional<ShopEntity> getMostViewedShop() {
        return shopRepository.findTopByOrderByShopHitDesc();
    }
	
	public List<UserWithPostAndCommentCount> findAllUsersWithCounts() {
        List<UserEntity> users = userRepository.findAll();
        List<UserWithPostAndCommentCount> userCounts = new ArrayList<>();

        for (UserEntity user : users) {
        	int postCount = reviewRepository.countByUser(user);
        	int commentCount = shopReviewRepository.countByUser(user);
        	UserWithPostAndCommentCount dto = new UserWithPostAndCommentCount(user, postCount, commentCount);
        	userCounts.add(dto);
        }

        return userCounts;
    }
	public Page<NoticeEntity> getAllNotice(Pageable pageable){
		return noticerepository.findAll(pageable);
	}
	
	@Transactional
    public void deleteNotice(Long notNo) {
        noticeRepository.deleteById(notNo);
    }
	
	public Page<RecoveryEntity> getAllRecovery(Pageable pageable){
		return recoveryRepository.findAll(pageable);
	}
	
	// 복구 요청을 처리하는 메서드
    public void processRecovery(Long recoverNo) {
        // 복구 요청을 데이터베이스에서 가져오기
        RecoveryEntity recovery = recoveryRepository.findById(recoverNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 복구 요청을 찾을 수 없습니다: " + recoverNo));

        
        // 복구 처리 (여기서는 단순히 상태와 처리 날짜를 업데이트)
        recovery.setStatus("COMPLETED");
        recovery.setProcessedDate(LocalDate.now());
        
        UserEntity user = recovery.getUser();
        user.setUDeactivated(false);
        
        userRepository.save(user);

        // 변경된 복구 요청을 저장
        recoveryRepository.save(recovery);
    }
    
    public List<CouponEntity> getAllCoupons() {
        return couponRepository.findAll();
    }
    public List<CouponMapEntity> getAllUserCoupons() {
        return couponMap.findAll();
    }
    public Optional<CouponEntity> findCouponById(Long couponNo) {
        return couponRepository.findById(couponNo);
    }
    
    public void deleteCouponById(Long couponMapId) {
        if (couponMap.existsById(couponMapId)) {
        	couponMap.deleteById(couponMapId);
        } else {
            throw new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다.");
        }
    }
    
    public void updateCouponState(Long couponNo, String state) {
        CouponEntity coupon = couponService.findCouponById(couponNo);
        coupon.setCouponState(state);
        couponRepository.save(coupon);
    }
    
    @Transactional
    public void recoveryUser(Long userUNo) {
    	UserEntity user = userRepository.findByUNo(userUNo);
    	
    	user.setUDeactivated(false);
    	userRepository.save(user);
    	
    }
    //shop and counts
    public List<ShopListAndOrderedDTO> getAllShopsAndCounts(){
    	List<ShopEntity> shops = shopRepository.findAll();
    	List<ShopListAndOrderedDTO> shopCounts = new ArrayList<>();
    	
    	for(ShopEntity shop : shops) {
    		int orderedCount = orderedRepository.countByShopNo(shop.getShopNo());
    		ShopListAndOrderedDTO dto = new ShopListAndOrderedDTO(shop, orderedCount);
    		shopCounts.add(dto);
    	}
    	
		return shopCounts;
	}
    // 구매내역
    public Page<OrderedEntity> getAllOrdered(Pageable pageable){
		return orderedRepository.findAll(pageable);
	}
    //상점 비활성화 기능
    @Transactional
    public void toggleShopActivation(Long shopNo, boolean isDeactivated) {
    	System.out.println("isDeactivated : " + isDeactivated);
        ShopEntity shop = shopRepository.findById(shopNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다: " + shopNo));
        
        // 비활성화 처리
        shop.setSDeactivated(isDeactivated);
        shopRepository.save(shop);
    }
}
