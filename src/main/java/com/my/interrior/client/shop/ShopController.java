package com.my.interrior.client.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ShopController {
    
	private static final int PAGE_SIZE = 10;
	
    @Autowired
    private ShopService shopService;
    
    @GetMapping("/shopWrite")
    public String shopWrite() {
        return "client/shop/shopWrite";
    }
    
    @PostMapping("/shopWrite")
    public String shopWrite(
            @RequestParam("shopTitle") String shopTitle,
            @RequestParam("shopPrice") String shopPrice,
            @RequestParam("shopContent") String shopContent,
            @RequestParam("shopCategory") String shopCategory,
            @RequestParam("shopMainPhoto") MultipartFile shopMainPhoto,
            @RequestParam("descriptionImages") MultipartFile[] descriptionImages,
            @RequestParam("optionName[]") List<String> optionNames,
            @RequestParam("shopDiscountRate") String shopDiscountRate,
            @RequestParam("option[]") List<String> options,
            @RequestParam("stock[]") List<Integer> stocks) throws IOException {
    	System.out.println("우선 보내긴");
        // Main Photo 업로드
        String shopMainPhotoUrl = shopService.uploadFile(shopMainPhoto, shopTitle);
        
        // Description Images 업로드
        List<String> descriptionImageUrls = new ArrayList<>();
        for (MultipartFile file : descriptionImages) {
            String url = shopService.uploadFile(file, shopTitle);
            descriptionImageUrls.add(url);
        }
        
        // Shop 정보 저장
        shopService.shopWrite(shopTitle, shopPrice,  shopContent, shopMainPhotoUrl, descriptionImageUrls, shopCategory, optionNames, options, stocks, shopDiscountRate);
        System.out.println("확인");
        return "client/review/auth/reviewList"; // 저장 후 목록 페이지로 리다이렉션
    }
    
    @GetMapping("/auth/shopList")
    public String shopList(Model model, Pageable pageable) {
    	Page<ShopEntity> shops = shopService.getAllShop(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
    	model.addAttribute("shoplist", shops.getContent());
    	model.addAttribute("currentPage", pageable.getPageNumber());
    	model.addAttribute("totalPages", shops.getTotalPages());
    	
    	for (ShopEntity shop : shops.getContent()) {
            System.out.println("Shop No: " + shop.getShopNo());
            System.out.println("Title: " + shop.getShopTitle());
            System.out.println("Discount Rate: " + shop.getShopDiscont());
            System.out.println("Price: " + shop.getShopPrice());
            System.out.println("Image URL: " + shop.getShopMainPhoto());
            System.out.println(); // 개행
        }
    	
    	return "client/shop/shopList";
    }
}
