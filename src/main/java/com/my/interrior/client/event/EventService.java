package com.my.interrior.client.event;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.Storage;
import com.my.interrior.admin.coupon.CouponRepository;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;

import jakarta.servlet.http.HttpSession;

@Service
public class EventService {
	
	@Autowired
	private Storage storage;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;
	
	public String uploadFile(MultipartFile file) throws IOException {
		// Event라는 풀더 안에 사진 저장
		String folderName = "Event";
		// 경로설정 폴더이름 /uuid-원래 파일이름
		String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		BlobId blobId = BlobId.of(bucketName, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
		storage.create(blobInfo, file.getBytes());
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
	}
	
	public void saveEvent(String eventTitle, String eventContent, MultipartFile eventImg, Long couponNo) throws IOException {
		System.out.println("이벤트 저장 들어옴");
		EventEntity eventEntity = new EventEntity();
		//쿠폰 불러오기
		 CouponEntity coupon = couponRepository.findById(couponNo)
	                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 쿠폰 번호입니다."));
		//gcs에 사진 파일 보내기
		 
		System.out.println("이벤트 이미지 : " + eventImg);
		String url = uploadFile(eventImg);
		//이벤트 엔티티에 저장
		eventEntity.setEventImg(url);
		eventEntity.setCoupon(coupon);
		eventEntity.setEventTitle(eventTitle);
		eventEntity.setEventContent(eventContent);
		
		eventRepository.save(eventEntity);
	}

}