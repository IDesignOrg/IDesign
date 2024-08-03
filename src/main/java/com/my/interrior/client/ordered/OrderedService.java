package com.my.interrior.client.ordered;

import java.time.LocalDate;
import java.util.List;

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
    	//자꾸 LazyException 걸림 cartEntity의 cartOptions() 때문.
//        List<CartEntity> cartEntities = (List<CartEntity>) session.getAttribute("cartEntities");
        String userId = (String) session.getAttribute("UId");
        List<CartEntity> cartEntities = cartRepository.findByUserEntity_UId(userId);

        UserEntity user = userRepository.findByUId(userId);

        for (CartEntity cartEntity : cartEntities) {
        	
        	//1. cartOptions 초기화 안 됨.
//        	cartEntity.getCartOptions().size();
        	//2 프록시 객체 초기화 안 됨
//        	Hibernate.initialize(cartEntity.getCartOptions());
        	
            OrderedEntity ordered = new OrderedEntity();
            ordered.setOrderedNumber(orNum());
            ordered.setOrderedState("주문 완료");
            ordered.setShipmentState("배송 준비중");
            ordered.setOrderedDate(LocalDate.now());
            ordered.setUserEntity(user);
            ordered.setShopNo(cartEntity.getShopEntity().getShopNo());

            System.out.println("ordered에는 : " + ordered);

            orderedRepository.save(ordered);
        }
    }
    
    public List<OrderedEntity> getOrderedList(HttpSession session){
    	String userId = (String) session.getAttribute("UId");
    	return orderedRepository.findByUserEntity_UId(userId);
    }

    public static Long orNum() {
        orderedNumber += 1;
        return (long) orderedNumber;
    }
}
