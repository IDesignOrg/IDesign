package com.my.interrior.client.evaluation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class ReviewService {
	// GCS 빈 가져오기
	@Autowired
	private Storage storage;
	@Autowired
	// 레퍼지토리 가져오기
	private ReviewRepository reviewRepository;
	// 세션값 받아오기
	@Autowired
	private HttpSession session;
	@Autowired
	private ReviewPhotoRepository reviewPhotoRepository;
	@Autowired
	private UserRepository userRepository;

	// 버킷 이름 가져오기
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;

	public String uploadFile(MultipartFile file) throws IOException {
		String userId = (String) session.getAttribute("UId");
		String folderName = "user_" + userId;
		String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		BlobId blobId = BlobId.of(bucketName, fileName);
		System.out.println("버킷 이름 : " + bucketName);
		System.out.println("파일 이름 : " + fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
		storage.create(blobInfo, file.getBytes());
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
	}

	@Transactional
	public void uploadFileAndCreateReview(String title, String category, String content, String starRating,
			MultipartFile[] files) throws IOException {

		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			String fileUrl = uploadFile(file);
			fileUrls.add(fileUrl);
		}

		// 작성자 id는 세션에서 가져옴
		String userId = (String) session.getAttribute("UId");

		System.out.println("세션값 유저 아이디 : " + userId);

		// UserEntity 객체 조회
		UserEntity userEntity = userRepository.findByUId(userId);

		System.out.println("유저 엔티티 확인 : " + userEntity);

		// 작성 시간 설정
		LocalDateTime writtenTime = LocalDateTime.now();

		// 조회수 초기화
		int views = 0;

		// 임시 프로필 string값
		String profile = "https://storage.googleapis.com/idesign/static/blank-profile-picture-973460_640.png";

		// 리뷰 저장
		ReviewEntity review = new ReviewEntity();

		Long rNo = review.getRNo();

		System.out.println("r_no값 확인 : " + rNo);
		review.setRTitle(title);
		review.setRCategory(category);
		review.setRContent(content);
		review.setRStarRating(starRating);
		review.setRProfile(profile); // 파일 URL을 프로필 이미지로 사용
		review.setRViews(views);
		review.setRWrittenTime(writtenTime);
		review.setUser(userEntity);
		reviewRepository.save(review);

		List<ReviewPhotoEntity> reviewPhotos = new ArrayList<>();
		for (String fileUrl : fileUrls) {
			ReviewPhotoEntity reviewPhoto = new ReviewPhotoEntity();
			reviewPhoto.setRpPhoto(fileUrl);
			reviewPhoto.setReview(review);
			reviewPhotos.add(reviewPhoto);
		}

		// reviewPhotoRepository 사용하여 사진 저장
		reviewPhotoRepository.saveAll(reviewPhotos);

	}

	// 페이징 처리된 후기 목록 가져오기
	public Page<ReviewEntity> getAllReviews(Pageable pageable) {
		return reviewRepository.findAll(pageable);
	}

	public Optional<ReviewEntity> getReviewById(Long rNo) {
		return reviewRepository.findById(rNo);
	}
}
