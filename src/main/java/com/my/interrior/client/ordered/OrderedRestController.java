package com.my.interrior.client.ordered;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.shop.ShopService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderedRestController {
    private final OrderedService orderedService;
    private final ShopService shopService;

    @PostMapping("/save/ordered")
    public ResponseEntity<?> saveOrdered(HttpSession session) {
        orderedService.saveOrdered(session);
        return ResponseEntity.ok("success");
    }
    
    @GetMapping("/my/ordered")
    public ResponseEntity<OrderedDTO> getOrdered(HttpSession session){
    	//여기에 merchant_uid, cancel_request_amount, reason, refund_holder, refund_bank
    	//등을 넣어줘야함.
    	OrderedDTO orderedDTO = new OrderedDTO();
    	List<OrderedEntity> orderedEntities = orderedService.getOrderedList(session);
    	List<Long> shopNo = orderedEntities.stream()
    						.map(order -> order.getShopNo())
    						.collect(Collectors.toList());
    	//만약 같은 게 있으면 중복이라 불러오지 못함 그래서 List를 하나 더 만들자
    	List<ShopEntity> shopEntities = new ArrayList<>();
    
    	for(int i = 0; i < shopNo.size(); i++) {
    		shopEntities.add(shopService.getShopEntityByShopNo(shopNo.get(i)));
    	}
    	
    	orderedDTO.setOrderedEntities(orderedEntities);
    	orderedDTO.setShopEntities(shopEntities);
    	
    	System.out.println("orderedDTO에는: " + orderedDTO);
    	
    	return ResponseEntity.ok(orderedDTO);
    }
}
