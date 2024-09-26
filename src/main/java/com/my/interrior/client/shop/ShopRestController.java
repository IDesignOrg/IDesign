package com.my.interrior.client.shop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.interrior.client.shop.shopDTO.ShopDTO;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Shop", description = "Shop API")
@RestController
@RequiredArgsConstructor
public class ShopRestController {

	private final ShopService shopService;
	
	//shop작성 
    @PostMapping("/shopWrite")
    public ResponseEntity<?> shopWrite(
    		@RequestPart("thumbnail") MultipartFile thumbnail, // 썸네일 파일
    	    @RequestPart("srcImage") List<MultipartFile> descriptionImages, // 설명 이미지 파일들
    	    @RequestPart("productData") String productDataJson) throws IOException {
    	// JSON 데이터를 객체로 변환 (productDataJson은 JavaScript에서 보낸 JSON)
        ObjectMapper objectMapper = new ObjectMapper();
        ShopDTO shopDTO = objectMapper.readValue(productDataJson, ShopDTO.class);

        // 썸네일 이미지 업로드 처리
        String shopMainPhotoUrl = shopService.uploadFile(thumbnail);

        // 설명 이미지 업로드 처리
        List<String> descriptionImageUrls = new ArrayList<>();
        for (MultipartFile file : descriptionImages) {
            String url = shopService.uploadFile(file);
            descriptionImageUrls.add(url);
        }

        // 데이터 저장 로직 실행
        shopService.shopWrite(shopDTO, shopMainPhotoUrl, descriptionImageUrls);
        Map<String, String> response = new HashMap<>();
        String success = "success";
        response.put("response", success);
        
        return ResponseEntity.ok(response);

    }
}
