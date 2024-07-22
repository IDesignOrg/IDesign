package com.my.interrior.client.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    
    //shop작성 
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
            @RequestParam("price[]") List<String> price) throws IOException {
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
        shopService.shopWrite(shopTitle, shopPrice,  shopContent, shopMainPhotoUrl, descriptionImageUrls, shopCategory, optionNames, options, price, shopDiscountRate);
        System.out.println("확인");

        return "client/shop/shopList";

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
    //shop디테일 
    @GetMapping("/auth/shopDetail/{shopNo}")
    public String shopDetail(@PathVariable("shopNo") Long shopNo, Model model) {
    	Optional<ShopEntity> shops = shopService.getShopById(shopNo);
    	model.addAttribute("shops", shops.get());
    	List<ShopPhotoEntity> shopPhoto = shopService.getShopPhotoById(shopNo);
    	model.addAttribute("shopPhotos", shopPhoto);
    	List<ShopOptionEntity> shopOptions = shopService.getAllShopOptions();
    	model.addAttribute("shopOption", shopOptions);
    	
    	
    	//색깔, 크기만 리스트로 가져옴 service에서는 findByshopEntity_shopNo(shopNo)
    	/*List<ShopOptionEntity> shopOptions = shopService.getShopOptionById(shopNo);
    	
    	List<String>shopOptionName = new ArrayList<>();
    	List<Long> shopOptionNo = new ArrayList<>();
    	
    	for(ShopOptionEntity optionName : shopOptions) {
    		shopOptionName.add(optionName.getShopOptionName());
    	}
    	model.addAttribute("optionName", shopOptionName);
    	
    	for(ShopOptionEntity optionNo : shopOptions) {
    		shopOptionNo.add(optionNo.getShopOptionNo());
    	}
    	model.addAttribute("optionNo", shopOptionNo);
    	
    	//색깔, 크기만 모델에 담음 뿌릴 때는 포이치문으로 ㄱ
    	//shopOptionName들만 가져옴.
    	//List<String> shopOptionName = shopOPtions.getShopOptionName();
    	//List<Long> shopOptionNo = shopOPtions.getShopOptionNo();
    	//model.addAttribute("shopOptionName", shopOptionName);
    	//shopOptionNo는 List형식ß
    	//repository에서는 List<String> findByshopOptionEntity_shopOptionNo(List<Long> shopOptionNo)
    	List<ShopOptionValueEntity> shopOptionValues = shopService.getShopOptionValueByshopOptionNo(shopOptionNo);
    	//다크, 딥다크, 로우다크등 가져옴
    	//model.addAttribute("shopOptionValues", shopOptionValues);*/
    	
    	
    	
    	return "client/shop/shopDetail";
    }
}
