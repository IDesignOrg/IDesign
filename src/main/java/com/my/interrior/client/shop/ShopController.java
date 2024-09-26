package com.my.interrior.client.shop;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.interrior.client.shop.shopDTO.ShopDTO;

import jakarta.servlet.http.HttpSession;

@Controller
public class ShopController {
    
	private static final int PAGE_SIZE = 10;
	
    @Autowired
    private ShopService shopService;
    
    
    @GetMapping("/shopWrite")
    public String shopWrite() {
        return "client/shop/shopWrite";
    }
    
    //shop 리스트 
    @GetMapping("/auth/shopList")
    public String shopList(Model model, Pageable pageable) {
    	Page<ShopEntity> shops = shopService.getAllShop(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
    	model.addAttribute("shoplist", shops.getContent());
    	model.addAttribute("currentPage", pageable.getPageNumber());
    	model.addAttribute("totalPages", shops.getTotalPages());
    	
    	return "client/shop/shopList";
    }
    @GetMapping("/auth/search")
    public String shopList(
    		@RequestParam(name = "shopTitle", required = false) String shopTitle,
    	    @RequestParam(name = "shopCategory", required = false) String shopCategory,
    	    @RequestParam(name = "minPrice", required = false) Integer minPrice,
    	    @RequestParam(name = "maxPrice", required = false) Integer maxPrice,
        Model model,
        Pageable pageable
    ) {
        Page<ShopEntity> shops = shopService.searchShops(shopTitle, shopCategory, minPrice, maxPrice, PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
       
        model.addAttribute("shoplist", shops.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", shops.getTotalPages());

        // 검색 조건을 다시 view에 전달하여 검색 폼에 값이 유지되도록 합니다.
        model.addAttribute("shopTitle", shopTitle);
        model.addAttribute("shopCategory", shopCategory);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        
        return "client/shop/shopList";
    }
    //shop디테일 
    @GetMapping("/auth/shopDetail/{shopNo}")
    public String shopDetail(@PathVariable("shopNo") Long shopNo, Model model, HttpSession session) {
    	String pageKey = "viewedShop_" + shopNo;
        LocalDateTime lastViewedTime = (LocalDateTime) session.getAttribute(pageKey);

        if (lastViewedTime == null || lastViewedTime.isBefore(LocalDateTime.now().minusHours(1))) {
            shopService.increaseViewCount(shopNo);

            // 현재 시간을 세션에 저장
            session.setAttribute(pageKey, LocalDateTime.now());
        }
    	Optional<ShopEntity> shops = shopService.getShopById(shopNo);
    	model.addAttribute("shops", shops.get());
    	List<ShopPhotoEntity> shopPhoto = shopService.getShopPhotoById(shopNo);
    	model.addAttribute("shopPhotos", shopPhoto);
    	List<ShopOptionEntity> shopOptions = shopService.getShopOptionById(shopNo);
    	model.addAttribute("shopOption", shopOptions);
    	
    	List<ShopReviewEntity> shopReviews = shopService.getShopReviewsByShopNo(shopNo);
        model.addAttribute("shopReviews", shopReviews);

        Map<Long, List<ShopReviewPhotoEntity>> reviewPhotosMap = new HashMap<>();
        for (ShopReviewEntity review : shopReviews) {
            List<ShopReviewPhotoEntity> reviewPhotos = shopService.getShopReviewPhotosByReviewNo(review.getShopReviewNo());
            reviewPhotosMap.put(review.getShopReviewNo(), reviewPhotos);
        }
        model.addAttribute("reviewPhotosMap", reviewPhotosMap);
	
    	return "client/shop/shopDetail";
    }
    
    @GetMapping("/shop/shopUpdate/{shopNo}")
    public String shopUpdate(Pageable pageable, Model model, @PathVariable("shopNo") Long shopNo) {
    	Optional<ShopEntity> shop = shopService.getShopById(shopNo);
    	System.out.println("shopNo의 값 : " + shopNo);
    	model.addAttribute("shops", shop);
    	
    	List<ShopPhotoEntity> shopPhotos = shopService.getShopPhotoById(shopNo);
    	model.addAttribute("shopPhoto", shopPhotos);
    	return "client/shop/shopUpdate";
    }
    
    //리뷰 작성
    @GetMapping("/shopReview/{shopNo}")
    public String shopReview(Model model, @PathVariable("shopNo") Long shopNo) {
    	Optional<ShopEntity> shops = shopService.getShopById(shopNo);
    	model.addAttribute("shops", shops.get());
    	return "client/shop/shopReview";
    }
    //리뷰 작성
    @PostMapping("/shopReview")
    public String shopReview(
    		@RequestParam("shopNo") Long shopNo,
            @RequestParam("starpoint") double starpoint,
            @RequestParam("shopContent") String shopContent,
            @RequestParam("descriptionImages") MultipartFile[] descriptionImages,
            RedirectAttributes redirectAttributes) {
    	try {
            shopService.shopReviewWrite(shopNo, starpoint, shopContent, descriptionImages);
            redirectAttributes.addFlashAttribute("message", "리뷰가 성공적으로 작성되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "리뷰 작성 중 오류가 발생했습니다: " + e.getMessage());
        }
    return "redirect:/auth/shopDetail/" + shopNo;
    }
    //리뷰 삭제
    @DeleteMapping("/shopReview/{shopReviewNo}")
    @ResponseBody
    public String deleteShopReview(@PathVariable("shopReviewNo") Long shopReviewNo) {
        shopService.deleteShopReview(shopReviewNo);
        return "Review deleted successfully";
    }
}
