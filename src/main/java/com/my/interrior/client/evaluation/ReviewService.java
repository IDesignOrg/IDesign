package com.my.interrior.client.evaluation;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
import com.my.interrior.client.evaluation.DTO.CommentResponseDto;
import com.my.interrior.client.gcs.GCSFileDeleter;
import com.my.interrior.client.shop.ShopEntity;
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
	@Autowired
	private GCSFileDeleter gcsFileDeleter;
	@Autowired
	private ReviewCommentRepository reviewCommentRepository;

	// 버킷 이름 가져오기
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;

	// gcs파일 업로드
	public String uploadFile(MultipartFile file) throws IOException {
		// 세션값 받아오기
		String userId = (String) session.getAttribute("UId");
		// 풀더 생성을 위해 user_ + 세션값으로 받기
		String folderName = "user_" + userId;
		// 경로설정 풀더이름 /uuid-원래 파일이름
		String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
		BlobId blobId = BlobId.of(bucketName, fileName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
		storage.create(blobInfo, file.getBytes());
		return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
	}

	// 후기 업로드
	@Transactional
	public void uploadFileAndCreateReview(String title, String category, String content, String starRating,
			MultipartFile[] files, MultipartFile mainPhoto) throws IOException {

		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			String fileUrl = uploadFile(file);
			fileUrls.add(fileUrl);
		}

		// 썸네일 사진 업로드
		String mainPhotoUrl = uploadFile(mainPhoto);
		// 작성자 id는 세션에서 가져옴
		String userId = (String) session.getAttribute("UId");

		// UserEntity 객체 조회
		UserEntity userEntity = userRepository.findByUId(userId);

		// 작성 시간 설정
		LocalDateTime writtenTime = LocalDateTime.now();

		// 조회수 초기화
		int views = 0;

		// 임시 프로필 string값

		// 리뷰 저장
		ReviewEntity review = new ReviewEntity();

		Long rNo = review.getRNo();

		
		review.setRTitle(title);
		review.setRCategory(category);
		review.setRContent(content);
		review.setRStarRating(starRating);
		review.setRMainPhoto(mainPhotoUrl);
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

	// 페이징 처리된 후기 목록 가져오기(사진)
	public List<ReviewPhotoEntity> getReviewPhotosByReviewNo(Long rNo) {
		return reviewPhotoRepository.findByReview_RNo(rNo);
	}

	// n+1 문제 해결된 리뷰 조회
	public Optional<ReviewEntity> getReviewById(Long rNo) {
		//새로 추가한 쿼리문
		return reviewRepository.findByIdWithUser(rNo);
	}

	// N+1 문제 해결된 리뷰 사진
	public List<ReviewPhotoEntity> getPhotosByReviewId(Long rNo) {
        // 최적화된 쿼리 사용
        return reviewPhotoRepository.findByReviewIdOptimized(rNo);
    }

	// 조회수 증가
	public void increaseViewCount(Long rNo) {
		ReviewEntity review = reviewRepository.findByIdWithUser(rNo)
		        .orElseThrow(() -> new NoSuchElementException("해당 리뷰를 찾을 수 없습니다."));
		    
		review.setRViews(review.getRViews() + 1);
		reviewRepository.save(review);
	}

	//N+1을 해결한 리뷰 사진 조회
	public List<ReviewCommentEntity> getCommentsByReviewId(Long reviewId) {
		
		 return reviewCommentRepository.findByReviewIdWithUser(reviewId);
	}

	//댓글 추가 
	public CommentResponseDto addComment(Long reviewId, String userIdFromSession, String comment) {
	    // ReviewEntity를 조회
	    ReviewEntity reviewEntity = reviewRepository.findById(reviewId)
	            .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + reviewId));

	    // UserEntity를 조회
	    UserEntity userEntity = userRepository.findByUId(userIdFromSession);
	    if (userEntity == null) {
	        throw new IllegalArgumentException("Invalid user ID: " + userIdFromSession);
	    }

	    // ReviewCommentEntity 생성 및 설정
	    ReviewCommentEntity reviewCommentEntity = new ReviewCommentEntity();
	    reviewCommentEntity.setReviewEntity(reviewEntity);
	    reviewCommentEntity.setUser(userEntity);
	    reviewCommentEntity.setRComment(comment);
	    reviewCommentEntity.setRCommentCreated(LocalDateTime.now());

	    // 엔티티를 저장
	    ReviewCommentEntity savedComment = reviewCommentRepository.save(reviewCommentEntity);

	    // 엔티티에서 필요한 값 추출하여 DTO로 변환
	    return new CommentResponseDto(
	        savedComment.getRCommentNo(),              // 댓글 번호
	        savedComment.getRComment(),                // 댓글 내용
	        savedComment.getRCommentCreated(),         // 댓글 작성 시간
	        userEntity.getUName(),                      // 사용자 이름 (UserEntity에서 추출)
	        userEntity.getUId(),                       //사용자 아이디 (UserEntity에서 추출)
	        userEntity.getUPofile()                    // 사용자 프로필 이미지 (UserEntity에서 추출)
	    );
	}


	public void deleteComment(Long commentId) {
		reviewCommentRepository.deleteById(commentId);
	}

	@Transactional
	public void updateReview(Long rNo, String title, String category, String content, String starRating,
			MultipartFile[] files, MultipartFile mainPhoto) throws IOException {

		// 썸네일 사진 업로드 없을시에 null값
		String mainPhotoUrl = null;
		if (!mainPhoto.isEmpty()) {
			mainPhotoUrl = uploadFile(mainPhoto);
		}

		// 리뷰 조회 또는 새로 생성
		ReviewEntity review = reviewRepository.findByIdWithUser(rNo)
		        .orElseThrow(() -> new NoSuchElementException("해당 리뷰를 찾을 수 없습니다."));
		    

		// 리뷰 정보 설정
		review.setRTitle(title);
		review.setRCategory(category);
		review.setRContent(content);
		review.setRStarRating(starRating);
		if (mainPhotoUrl != null) {
			String deletephoto = review.getRMainPhoto();
			gcsFileDeleter.deleteFile(deletephoto);
			review.setRMainPhoto(mainPhotoUrl);
		}
		// 리뷰 저장
		reviewRepository.save(review);

		List<String> fileUrls = new ArrayList<>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				String fileUrl = uploadFile(file);
				fileUrls.add(fileUrl);
			}
		}

		// 기존 리뷰 사진 삭제
		if (rNo != null && !fileUrls.isEmpty()) {
			List<ReviewPhotoEntity> reviewPhotoDel = reviewPhotoRepository.findByReview_RNo(rNo);
			for (ReviewPhotoEntity photo : reviewPhotoDel) {
				gcsFileDeleter.deleteFile(photo.getRpPhoto());
				reviewPhotoRepository.delete(photo);
			}
		}

		// 새로운 리뷰 사진 저장
		if (!fileUrls.isEmpty()) {
			List<ReviewPhotoEntity> reviewPhotos = new ArrayList<>();
			for (String fileUrl : fileUrls) {
				ReviewPhotoEntity reviewPhoto = new ReviewPhotoEntity();
				reviewPhoto.setRpPhoto(fileUrl);
				reviewPhoto.setReview(review);
				reviewPhotos.add(reviewPhoto);
			}
			reviewPhotoRepository.saveAll(reviewPhotos);
		}

	}
	@Transactional
	public void deleteReview(Long rNo) {
		if (rNo == null || rNo <= 0) {
            throw new IllegalArgumentException("유효하지 않은 리뷰 번호입니다.");
        }

        // 리뷰가 존재하는지 확인 후 삭제
        if (!reviewRepository.existsById(rNo)) {
            throw new NoSuchElementException("해당 리뷰가 존재하지 않습니다.");
        }
		ReviewEntity reviewEntity = reviewRepository.findByIdWithUser(rNo).orElse(null);
		

		String deleteGCSFileName = reviewEntity.getRMainPhoto();
		gcsFileDeleter.deleteFile(deleteGCSFileName);
		List<ReviewPhotoEntity> reviewPhotoDel = reviewPhotoRepository.findByReview_RNo(rNo);
		for (ReviewPhotoEntity photo : reviewPhotoDel) {
			gcsFileDeleter.deleteFile(photo.getRpPhoto());
		}
		reviewPhotoRepository.deleteByReviewRNo(rNo);
		reviewCommentRepository.deleteByReviewEntityRNo(rNo);
		reviewRepository.deleteById(rNo);
	}

}
