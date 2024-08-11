package com.my.interrior.client.ordered;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.my.interrior.client.cart.CartEntity;
import com.my.interrior.client.cart.CartRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderedService {

	private final UserRepository userRepository;
	private final OrderedRepository orderedRepository;
	private final CartRepository cartRepository;
	private static int orderedNumber = 100000;

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
			ordered.setShipmentState("배송 준비중");
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

	@Scheduled(cron = "0 0 0 * * *") // 매일 자정에 실행
	@Transactional
	public void updateShipmentStatus() {
		List<OrderedEntity> orders = orderedRepository.findAll();

		for (OrderedEntity order : orders) {
			LocalDate orderedDate = order.getOrderedDate();
			LocalDate today = LocalDate.now();

			if (orderedDate.equals(today)) {
				order.setShipmentState("배송 준비중");
			} else if (orderedDate.equals(today.minusDays(1))) {
				order.setShipmentState("배송중");
			} else if (orderedDate.isBefore(today.minusDays(1))) {
				order.setShipmentState("배송완료");
			}
			
			orderedRepository.save(order);
		}
	}

	@Transactional
	public void deleteByMerchantUId(String merchantUId) {
		orderedRepository.deleteByMerchantUId(merchantUId);
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
