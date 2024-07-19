package com.my.interrior.client.shop;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class ShopService {
    
    @Autowired
    private Storage storage;

    @Autowired
    private ShopRepository shopRepository;
    
    @Autowired
    private ShopOptionRepository shopOptionRepository;
    
    @Autowired
    private ShopPhotoRepository shopPhotoRepository;

    @Autowired
    private HttpSession session;
    
    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    
    // GCS 파일 업로드
    public String uploadFile(MultipartFile file, String shopTitle) throws IOException {
        // 세션값 받아오기
        String userId = (String) session.getAttribute("UId");
        // 폴더 생성을 위해 user_ + 세션값으로 받기
        String folderName = "user_" + userId;
        String shopName = "shop_" + shopTitle;
        // 경로설정 폴더이름 /uuid-원래 파일이름
        String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
    
    @Transactional
    public void shopWrite(String shopTitle, String shopPrice, String shopContent, String shopMainPhotoUrl, 
                          List<String> descriptionImageUrls, String shopCategory, List<String> optionNames, 
                          List<String> options, List<Integer> stocks, String shopDiscountRate) {
        // ShopEntity 저장
    	System.out.println("서비스 들어오긴");
        ShopEntity shopEntity = new ShopEntity();
        int hits = 0;
        int sell = 0;
        shopEntity.setShopTitle(shopTitle);
        shopEntity.setShopDiscont(shopDiscountRate);
        shopEntity.setShopContent(shopContent);
        shopEntity.setShopMainPhoto(shopMainPhotoUrl);
        shopEntity.setShopPrice(shopPrice);
        shopEntity.setShopHit(hits);
        shopEntity.setShopSell(sell);
        shopEntity.setShopCategory(shopCategory);
        shopEntity.setShopWriteTime(LocalDateTime.now());

        shopRepository.save(shopEntity);


        // ShopOptionEntity 저장
        for (int i = 0; i < optionNames.size(); i++) {
            ShopOptionEntity optionEntity = new ShopOptionEntity();
            optionEntity.setShopEntity(shopEntity);
            optionEntity.setShopOptionName(optionNames.get(i));
            optionEntity.setShopOptionValue(options.get(i));
            optionEntity.setShopOptionStock(stocks.get(i));
            shopOptionRepository.save(optionEntity);
        }

        // Description Images URL 저장
        for (String url : descriptionImageUrls) {
            ShopPhotoEntity photoEntity = new ShopPhotoEntity();
            photoEntity.setShopEntity(shopEntity);
            photoEntity.setShopPhotoUrl(url);
            shopPhotoRepository.save(photoEntity);
        }
    }
    
    //샵 페이지 리스트
    public Page<ShopEntity> getAllShop(Pageable pageable){
    	return shopRepository.findAll(pageable);
    }
}
