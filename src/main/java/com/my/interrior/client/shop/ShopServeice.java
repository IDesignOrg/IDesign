package com.my.interrior.client.shop;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class ShopServeice {
	@Autowired
	private Storage storage;

	@Autowired
	private ShopRepository shopRepository;
	
	@Autowired
	private HttpSession session;
	
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;
	
	//gcs파일 업로드
	public String uploadFile(MultipartFile file, String shopTitle) throws IOException {
		// 세션값 받아오기
		String userId = (String) session.getAttribute("UId");
		// 풀더 생성을 위해 user_ + 세션값으로 받기
		String folderName = "user_" + userId;
		String shopName = "shop_" + shopTitle;
		// 경로설정 풀더이름 /uuid-원래 파일이름
		String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		BlobId blobId = BlobId.of(bucketName, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
		storage.create(blobInfo, file.getBytes());
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
	}	
	
	@Transactional
	public void shopWrite(String shopTitle, String shopPrice, String shopContent, String shopMainPhotoUrl, 
    List<String> descriptionImageUrls, List<String> optionNames, List<String> options, 
    List<Integer> stocks) {
		
	}
}
	
