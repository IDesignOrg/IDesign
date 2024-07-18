package com.my.interrior.client.shop;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@Controller
public class ShopController {
	
	@GetMapping("auth/shopWrite")
	public String shopWrite() {
		return "client/shop/shopWrite";
	}
	@PostMapping("/auth/shopWrite")
	public String shopWrite(
			@RequestParam("shopTitle") String shopTitle,
            @RequestParam("shopPrice") String shopPrice,
            @RequestParam("shopContent") String shopContent,
            @RequestParam("shopMainPhoto") MultipartFile shopMainPhoto, // GCS 사용으로 주석 처리
            @RequestParam("descriptionImages") MultipartFile[] descriptionImages, // GCS 사용으로 주석 처리
            @RequestParam("optionName[]") List<String> optionNames,
            @RequestParam("option[]") List<String> options,
            @RequestParam("stock[]") List<Integer> stocks) {
		
		return "andsga";
	}
	
}
