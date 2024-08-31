package com.my.interrior.three;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ThreeRestController {

	private final UserRepository userRepository;
	private final ThreeRepository threeRepository;
	private final PointRepository pointRepository;
	private final DataRepository dataRepository;
	private final ThreeService threeService;
	private final ChildRelationshipRepository childRelationshipRepository;
	
	
	@PostMapping("/save/project")
	public ResponseEntity<?> saveProject(@RequestPart("jsonData") SaveProjectRequest request,
			@RequestPart("file") MultipartFile thumbnail,
			HttpSession session) throws IOException{
		String userId = (String) session.getAttribute("UId");
		
		log.info("request DATA: {}, request FILE: {}", request, thumbnail);
		
		threeService.saveData(request, thumbnail, userId);
		
		return ResponseEntity.ok().build();
	}
	
	
	
//	// 나중에 RestController로 옮길 예정
//		@Transactional
//		@PostMapping("/save/project")
//		public void saveProject(@RequestBody SaveProjectRequest request, HttpSession session) throws Exception {
//			// userNo와 project ID 추출하기
//			
//			String userId = (String) session.getAttribute("UId");
//			
//			UserEntity user = userRepository.findByUId(userId);
//			String projectId = request.getProjectId();
//
//			log.info("userId : {}, projectId : {}, user : {}", userId, projectId, user);
//			//저장하기 전에 ThreeEntity 먼저 저장시켜야 함.
//			ThreeEntity threeEntity = new ThreeEntity();
//			threeEntity.setProjectId(projectId);
//			threeEntity.setUserEntity(user);
//			// three 저장
//			threeRepository.save(threeEntity);
//			
//			
//			// 데이터 엔티티 저장(dataEntity는 List로 되어있음)
//			for (SaveProjectRequest.DataEntityRequest dataRequest : request.getDataEntities()) {
//				log.info("dataRequest: {}, threeEntity: {}", dataRequest, threeEntity);
//				saveDataEntity(dataRequest, threeEntity, null);
//			}
//
//		}
//
//		//데이터 엔티티 저장
//		private void saveDataEntity(SaveProjectRequest.DataEntityRequest dataRequest, ThreeEntity threeEntity, DataEntity parentEntity) {
//			// dataEntity 하나하나 넣기
//			
//			log.info("dataRequest: {}", dataRequest);
//			
//			DataEntity data = new DataEntity();
//			data.setType(dataRequest.getType());
//			data.setAngle(dataRequest.getAngle());
//			data.setRotation(dataRequest.getRotation());
//			data.setThreeEntity(threeEntity); // three와 연결
//			data.setParent(parentEntity); // 부모와 연결(없을 시 null)
//			
//			dataRepository.save(data);
//			
//			//포인트 엔티티 저장
//			for(SaveProjectRequest.PointRequest pointRequest : dataRequest.getPoints()) {
//				PointEntity point = new PointEntity();
//				
//				point.setX(pointRequest.getX());
//				point.setY(pointRequest.getY());
//				point.setZ(pointRequest.getZ());
//				point.setDataEntity(data); //data와 연결
//				
//				pointRepository.save(point);
//			}
//			
//			//children 엔티티 저장
//			//여기 getChildren()의 데이터가 없음 + parent에 null값이 들어감 + dataEntity의 oid를 PK말고 
//			//새로 PK를 IDENTITY로 만들 필요가 있을 수도 있음.
//			for (SaveProjectRequest.DataEntityRequest childRequest : dataRequest.getChildren()) {
//				//저장하기
//				saveDataEntity(childRequest, threeEntity, data);
//			}
//		}
	
	
}
