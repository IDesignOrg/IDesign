package com.my.interrior.client.ordered;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.interrior.client.cart.CartEntity;
import com.my.interrior.client.cart.CartRepository;
import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.shop.ShopRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderedService {

	private final UserRepository userRepository;
	private final OrderedRepository orderedRepository;
	private final CartRepository cartRepository;
	private static int orderedNumber = 100000;
	private final OrderedRefundRepository orderedRefundRepository;
	private final ShopRepository shopRepository;

	@Transactional
	public void saveOrdered(HttpSession session) {
		// 자꾸 LazyException 걸림 cartEntity의 cartOptions() 때문.
//        List<CartEntity> cartEntities = (List<CartEntity>) session.getAttribute("cartEntities");
		String userId = (String) session.getAttribute("UId");
		String merchantUId = (String) session.getAttribute("merchantUId");
		List<CartEntity> cartEntities = cartRepository.findByUserEntity_UId(userId);

		UserEntity user = userRepository.findByUId(userId);

		System.out.println("merchantUIdInOrdered: " + merchantUId);
		for (CartEntity cartEntity : cartEntities) {

			// 1. cartOptions 초기화 안 됨.
//        	cartEntity.getCartOptions().size();
			// 2 프록시 객체 초기화 안 됨
//        	Hibernate.initialize(cartEntity.getCartOptions());

			OrderedEntity ordered = new OrderedEntity();
			ordered.setOrderedNumber(orNum());
			ordered.setOrderedState("주문 완료");
			ordered.setShipmentState("주문 확인 중");
			ordered.setOrderedDate(LocalDate.now());
			ordered.setUserEntity(user);
			// u_no랑 shop_no를 비교해서 quantity 가져오기
			Long userNo = user.getUNo();
			Long shopNo = cartEntity.getShopEntity().getShopNo();
			int quantity = cartEntity.getQuantity();
			ordered.setShopNo(shopNo);
			ordered.setMerchantUId(merchantUId);
			ordered.setQuantity(quantity);
			System.out.println("ordered에는 : " + ordered);

			orderedRepository.save(ordered);
		}
		session.removeAttribute("merchantUId");
	}
	//배송 시간마다 바뀌는거
	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Transactional
	public void updateShipmentStatus() {
		List<OrderedEntity> orders = orderedRepository.findAll();

		for (OrderedEntity order : orders) {
	        // 현재 주문의 배송 상태를 가져옵니다.
	        String orderedShipmentState = order.getShipmentState();

	        // 배송 상태가 "배송 준비중"일 때만 상태를 업데이트합니다.
	        if ("배송 준비중".equals(orderedShipmentState)) {
	            LocalDate orderedDate = order.getOrderedDate();
	            LocalDate today = LocalDate.now();

	            if (orderedDate.equals(today.minusDays(1))) {
	                order.setShipmentState("배송중");
	            } else if (orderedDate.isBefore(today.minusDays(1))) {
	                order.setShipmentState("배송완료");
	            }

	            orderedRepository.save(order);
	        }
		}
	}
		/*
		for (OrderedEntity order : orders) {
			LocalDate orderedDate = order.getOrderedDate();
			LocalDate today = LocalDate.now();

			
			if (orderedDate.equals(today.minusDays(1))) {
				order.setShipmentState("배송중");
			} else if (orderedDate.isBefore(today.minusDays(1))) {
				order.setShipmentState("배송완료");
			}

			orderedRepository.save(order);
		}
		*/
		

	@Transactional
	public void deleteByMerchantUId(String merchantUId) {
		orderedRepository.deleteByMerchantUId(merchantUId);
	}

	//환불시에 state변환
	@Transactional
	public void updateOrderedState(String merchantUId, String refundReason, String userid) {
		// merchantUId로 단일 OrderedEntity 조회
		OrderedEntity orderedEntity = orderedRepository.findByMerchantUId(merchantUId)
				.orElseThrow(() -> new EntityNotFoundException("No order found with merchantUId: " + merchantUId));

		// orderedState 업데이트
		orderedEntity.setOrderedState("환불");
		orderedEntity.setShipmentState("환불");

		// 변경된 엔티티 저장
		orderedRepository.save(orderedEntity);
		
		
		OrderedRefundEntity orderedRefundEntity = new OrderedRefundEntity();
	    orderedRefundEntity.setOrderedEntity(orderedEntity);  // 관련 주문 설정
	    orderedRefundEntity.setRefundReason(refundReason);  // 환불 사유 설정
	    orderedRefundEntity.setRefundUser(userid);
		//환불 사유 저장
		orderedRefundRepository.save(orderedRefundEntity);
		
		//shopRefund횟수 추가
		ShopEntity shopEntity = shopRepository.findById(orderedEntity.getShopNo())
	            .orElseThrow(() -> new EntityNotFoundException("No shop found with shopNo: " + orderedEntity.getShopNo()));

	    // 환불 횟수 증가
	    shopEntity.setShopRefundCount(shopEntity.getShopRefundCount() + 1);

	    // 변경된 ShopEntity 저장
	    shopRepository.save(shopEntity);
	}

	public List<OrderedEntity> getOrderedList(HttpSession session) {
		String userId = (String) session.getAttribute("UId");
		return orderedRepository.findByUserEntity_UId(userId);
	}

	public static Long orNum() {
		orderedNumber += 1;
		return (long) orderedNumber;
	}
}
