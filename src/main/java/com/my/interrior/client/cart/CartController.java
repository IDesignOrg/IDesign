package com.my.interrior.client.cart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.interrior.client.shop.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@PostMapping("/cart")
    public String goCart(
    		@RequestParam("shopNo") Long shopNo,
    		@RequestParam("options") List<Long> optionValueIds,
    		@RequestParam("quantity") int quantity, HttpSession session) {
    	
    	String userId = (String) session.getAttribute("UId");
    	
    	if(userId == null) {
    		return "redirect:/auth/login";
    	}
    	
    	cartService.inCart(optionValueIds, shopNo, quantity);

    	return "client/shop/shopList";
    }

}
