package com.my.interrior.admin.page;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.shop.ShopRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

@Service
public class AdminPageService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
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
	
	//전체 유저 정
	public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }
	
}
