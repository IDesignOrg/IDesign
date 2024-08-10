package com.my.interrior.admin.page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.notice.NoticeRepository;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.csc.recover.RecoveryRepository;
import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.shop.ShopRepository;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

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

        // 변경된 복구 요청을 저장
        recoveryRepository.save(recovery);
    }
	
}
