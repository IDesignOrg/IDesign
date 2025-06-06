package com.my.interrior.three;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

	public Page<ThreeEntity> getTopData(String userId, String title, int page, int size, String sort) {

		// 정렬 정보 설정
		Sort sortOrder;
		switch (sort) {
		case "recent":
			sortOrder = Sort.by(Sort.Order.desc("regDate"));
			break;
		case "asc":
			sortOrder = Sort.by(Sort.Order.asc("title"));
			break;
		case "desc":
			sortOrder = Sort.by(Sort.Order.desc("title"));
			break;
		default:
			sortOrder = Sort.by(Sort.Order.desc("regDate")); // 기본 정렬
			break;
		}

		// Pageable 객체 생성
		Pageable pageable = PageRequest.of(page, size, sortOrder);

		return threeRepository.findByUserEntity_UIdAndTitle(userId, title, pageable);
	}

	public int getCounts(String userId, String title, String sort) {
		int count = 0;
		if (title == null || title.isEmpty()) {
			count = threeRepository.countByUserEntity_UId(userId);
		} else {
			count = threeRepository.countByUserEntity_UIdAndTitleContaining(userId, title);
		}
		return count;
	}

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
	public String saveData(SaveProjectRequest request, MultipartFile thumbnail, String userId) throws IOException {
		UserEntity user = userRepository.findByUId(userId);
		String projectId = request.getProject_id();

		String file = uploadFile(thumbnail);

		System.out.println("file : " + file);

		log.info("userId : {}, projectId : {}, user : {}", userId, projectId, user);

		// 저장하기 전에 ThreeEntity 먼저 저장시켜야 함.
		ThreeEntity threeEntity = threeRepository.findByProjectId(projectId);
		threeEntity.setProjectId(projectId);
		threeEntity.setThumbnail(file);
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
		return projectId;
	}

	public SaveProjectRequest getProjectData(String projectId) {
		ThreeEntity three = threeRepository.findByProjectId(projectId);

		if (three == null)
			return null;

		SaveProjectRequest request = new SaveProjectRequest();

		request.setProject_id(projectId);
		request.setProjectSrc(new SaveProjectRequest.ProjectRequest());
		request.getProjectSrc().setTitle(three.getTitle());
		request.getProjectSrc().setSrc(three.getSrc());

		List<SaveProjectRequest.DataRequest> dataRequests = new ArrayList<>();
		List<DataEntity> dataEntities = dataRepository.findByThreeEntity(three);

		if (dataEntities == null)
			return null;

		for (DataEntity dataEntity : dataEntities) {
			SaveProjectRequest.DataRequest dataRequest = new SaveProjectRequest.DataRequest();
			dataRequest.setOid(dataEntity.getOid());
			dataRequest.setType(dataEntity.getType());
			dataRequest.setRotation(dataEntity.getRotation());
			dataRequest.setParent(dataEntity.getParent());
			dataRequest.setChildren(dataEntity.getChildren());

			List<SaveProjectRequest.PointRequest> pointRequests = new ArrayList<>();
			List<PointEntity> pointEntities = pointRepository.findByData(dataEntity);

			for (PointEntity pointEntity : pointEntities) {
				SaveProjectRequest.PointRequest pointRequest = new SaveProjectRequest.PointRequest();

				pointRequest.setX(pointEntity.getX());
				pointRequest.setY(pointEntity.getY());
				pointRequest.setZ(pointEntity.getZ());
				pointRequests.add(pointRequest);
			}
			dataRequest.setPoints(pointRequests);
			dataRequests.add(dataRequest);
		}
		request.setDataEntities(dataRequests);

		return request;
	}

	@Transactional
	public String removeProjects(List<String> projectIds) throws IOException {

		List<DataEntity> dataEntities = dataRepository.findByThreeEntity_ProjectIdIn(projectIds);

		System.out.println("dataEntities: " + dataEntities);
		
		
		if (dataEntities == null)
			return "fail";

		for (DataEntity data : dataEntities) {
			String oid = data.getOid();
			pointRepository.deleteByData_Oid(oid);
		}

		dataRepository.deleteAll(dataEntities);

		threeRepository.deleteByProjectIdIn(projectIds);

		return "success";
	}
}
