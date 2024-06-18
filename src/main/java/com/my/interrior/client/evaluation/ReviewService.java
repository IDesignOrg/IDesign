package com.my.interrior.client.evaluation;

import java.io.IOException;
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

@Service
public class ReviewService {
	private final Storage storage;
	
	@Autowired
    private ReviewRepository reviewRepository;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    public ReviewService(Storage storage) {
        this.storage = storage;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, fileName);
        System.out.println("버킷 이름 : " + bucketName);
        System.out.println("파일 이름 : " + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
        storage.create(blobInfo, file.getBytes());
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }
    
    // 페이징 처리된 후기 목록 가져오기
    public Page<ReviewEntity> getAllReviews(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public Optional<ReviewEntity> getReviewById(Long rNo) {
        return reviewRepository.findById(rNo);
    }
}
