package com.my.interrior.three;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ThreeService {

	private final UserRepository userRepository;
	private final ThreeRepository threeRepository;
	private final PointRepository pointRepository;
	private final DataRepository dataRepository;
	private final HttpSession session;
	@Value("${spring.cloud.gcp.storage.bucket}")
	private String bucketName;
//    private final ChildRelationshipRepository childRelationshipRepository;
	@Autowired
	private Storage storage;

	
	// GCS 파일 업로드
		public String uploadFile(MultipartFile file) throws IOException {
			// 세션값 받아오기
			String userId = (String) session.getAttribute("UId");
			// 폴더 생성을 위해 user_ + 세션값으로 받기
			String folderName = "three" + "user_" + userId;
			// 경로설정 폴더이름 /uuid-원래 파일이름
			String fileName = folderName + "/" + UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
			BlobId blobId = BlobId.of(bucketName, fileName);
			BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType(file.getContentType()).build();
			storage.create(blobInfo, file.getBytes());
			return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
		}
		
	@Transactional
	public void saveData(SaveProjectRequest request,
		MultipartFile thumbnail, String userId) throws IOException {
		UserEntity user = userRepository.findByUId(userId);
		String projectId = request.getProjectId();

		log.info("userId : {}, projectId : {}, user : {}", userId, projectId, user);
		log.info("thumbnail: {}, src: {}, title: {}", request.getThumbnail(), request.getProjectSrc().getSrc(), request.getProjectSrc().getTitle());

		// 저장하기 전에 ThreeEntity 먼저 저장시켜야 함.
		ThreeEntity threeEntity = new ThreeEntity();
		threeEntity.setProjectId(projectId);
		threeEntity.setUserEntity(user);
		threeEntity.setThumbnail(uploadFile(thumbnail));
		threeEntity.setSrc(request.getProjectSrc().getSrc());
		threeEntity.setTitle(request.getProjectSrc().getTitle());
		// three 저장
		threeRepository.save(threeEntity);

		// data 저장
		Map<String, DataEntity> dataMap = new HashMap<>();

		// Step 1: 모든 데이터 엔티티를 저장
		for (SaveProjectRequest.DataRequest dataRequest : request.getDataEntities()) {
			log.info("dataRequest의 값들 : {}", dataRequest);
			DataEntity data = new DataEntity();
			data.setOid(dataRequest.getOid());
			data.setType(dataRequest.getType());
			data.setRotation(dataRequest.getRotation());
			data.setParent(dataRequest.getParent());
			data.setChildren(dataRequest.getChildren());
			data.setThreeEntity(threeEntity);

			data = dataRepository.save(data);
			dataMap.put(data.getOid(), data);

			if (dataRequest.getPoints() != null) {
				for (SaveProjectRequest.PointRequest pointRequest : dataRequest.getPoints()) {
					PointEntity point = new PointEntity();
					point.setX(pointRequest.getX());
					point.setY(pointRequest.getY());
					point.setZ(pointRequest.getZ());
					point.setData(data);

					pointRepository.save(point);
				}
			}
		}
	}
}
