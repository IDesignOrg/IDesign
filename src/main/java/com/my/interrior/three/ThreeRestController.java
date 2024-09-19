package com.my.interrior.three;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ThreeRestController {

	private final ThreeService threeService;
	private final UserRepository userRepository;
	private final ThreeRepository threeRepository;

	@PostMapping("/save/project")
	public ResponseEntity<String> saveProject(@RequestPart("jsonData") SaveProjectRequest request,
			@RequestPart("file") MultipartFile thumbnail, HttpSession session) throws IOException {
		String userId = (String) session.getAttribute("UId");

		log.info("request DATA: {}, request FILE: {}", request, thumbnail);

		String projectId = threeService.saveData(request, thumbnail, userId);

		if (projectId != null)
			return ResponseEntity.ok(projectId);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
	}

	@DeleteMapping("/api/remove/projects")
	public ResponseEntity<?> removeProjects(@RequestParam("project_ids") List<Integer> projectIds){
		System.out.println("projectIds: " + projectIds);
		
		return ResponseEntity.ok().build();
	}
	@GetMapping("/api/get/project_nodes")
	public ResponseEntity<SaveProjectRequest> getProjectNodes(@RequestParam("project_id") String projectId)
			throws IOException {
		Optional<ThreeEntity> three = threeRepository.findById(projectId);

		String exec = three.get().getThumbnail();

		// 빈 데이터 반환
		if (exec.equals("pre"))
			return ResponseEntity.ok().build();

		SaveProjectRequest projectData = threeService.getProjectData(projectId);

		if (projectData == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok(projectData);
	}

	// dashboard에서 프로젝트 생성 클릭
	@Transactional
	@PostMapping("/api/create_project")
	public ResponseEntity<?> createProject(@RequestBody CreateProjectRequest createProjectRequest, HttpSession session)
			throws IOException {
		// UUID 생성 후 하이푼 제거
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		// 16자리만 가져오자.
		String project_id = uuid.substring(0, 16);

		String userId = (String) session.getAttribute("UId");

		if (userId == null)
			return ResponseEntity.ok().body("fail");

		UserEntity user = userRepository.findByUId(userId);
		// title, src, projectId만 넣고 나머지 값들은 일단 null을 넣자
		ThreeEntity three = new ThreeEntity();

		three.setTitle(createProjectRequest.getTitle());
		three.setSrc(createProjectRequest.getSrc());
		three.setProjectId(project_id);
		three.setDataEntity(null);
		three.setModDate(null);
		three.setRegDate(null);
		three.setThumbnail("none");
		three.setUserEntity(user);
		System.out.println("projectId: " + project_id);

		threeRepository.save(three);

		Map<String, String> response = new HashMap<>();

		response.put("project_id", project_id);
		return ResponseEntity.ok(response);

	}

	// filter : 빈 문자열, sort : 문자열, flag : 정수형 Request
	// response : status : success, fail || response: 200, errorcode || data:
	// {projects:[], flag_num : 0 => 0~11, 1=> 12 ~23}
	@GetMapping("/api/get/projects")
	public ResponseEntity<GetProjectsResponse> getProjects(@ModelAttribute("params") GetProjectsRequest request,
			HttpSession session) {
		String userId = (String) session.getAttribute("UId");
		String filter = request.getFilter();
		String sort = request.getSort();
		int page = request.getFlag(); // 페이지
		int size = 12; // 한 페이지에 가져올 데이터 수

		// 해당 사용자의 프로젝트 갯수 구하기(페이징 조건)
		int count = threeService.getCounts(userId, filter, sort);

		// 해당 사용자의 프로젝트 구하기
		// 사용자가 없으면(얼리 리턴 패턴)
		if (userId == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		// 만약 데이터베이스에 가지고 있는 데이터가 없으면
		if (count == 0) {
			GetProjectsResponse response = new GetProjectsResponse();
			response.setStatus("success");
			response.setResponse(200);
			GetProjectsResponse.GetProjectDataResponse data = new GetProjectsResponse.GetProjectDataResponse();
			data.setProjects(List.of()); // 빈 리스트 설정
			data.setFlagNum(-1); // 데이터 없음 플래그
			response.setData(data);
			// 임의의 데이터 생성
//	        GetProjectsResponse.GetProjectsData project1 = new GetProjectsResponse.GetProjectsData();
//	        project1.setProjectId("1");
//	        project1.setThumbnail("https://어쩌구저쩌구.jpg");
//	        project1.setTitle("테스트 1 테스트 11111111111111111111111");
//	        project1.setSrc("이건 설명입니다.");
//	        project1.setRegDt(LocalDateTime.now());
//
//	        GetProjectsResponse.GetProjectsData project2 = new GetProjectsResponse.GetProjectsData();
//	        project2.setProjectId("2");
//	        project2.setThumbnail("https://어쩌구저쩌구.jpghttps://어쩌구저쩌구.jpghttps://어쩌구저쩌구.jpg");
//	        project2.setTitle("테스트222222222222222222");
//	        project2.setSrc("이것도 설명입니다.");
//	        project2.setRegDt(LocalDateTime.now());

			// 데이터 리스트로 설정
//	        GetProjectsResponse.GetProjectDataResponse test = new GetProjectsResponse.GetProjectDataResponse();
//	        test.setProjects(List.of(project1, project2)); // 임의의 프로젝트 리스트
//	        test.setFlagNum(-1); // 데이터 없음 플래그
//	        
//	        // 응답에 데이터를 담아서 반환
//	        response.setData(test);
			return ResponseEntity.ok(response);
		}

		// 데이터 가져오기
		Page<ThreeEntity> pageResult = threeService.getTopData(userId, filter, page, size, sort);

		// 다음 페이지를 위한 flag 계산
		int nextFlag = pageResult.hasNext() ? 1 : -1;

		// 데이터가 없을 경우
		if (pageResult.getContent().isEmpty()) {
			GetProjectsResponse response = new GetProjectsResponse();
			response.setStatus("success");
			response.setResponse(200);
			GetProjectsResponse.GetProjectDataResponse data = new GetProjectsResponse.GetProjectDataResponse();
			data.setProjects(List.of()); // 빈 리스트 설정
			data.setFlagNum(-1); // 데이터 없음 플래그
			response.setData(data);
			return ResponseEntity.ok(response);
		}

		// GetProjectsResponse 객체 설정
		GetProjectsResponse response = new GetProjectsResponse();
		response.setStatus("success");
		response.setResponse(200);

		GetProjectsResponse.GetProjectDataResponse data = new GetProjectsResponse.GetProjectDataResponse();
		data.setProjects(pageResult.getContent().stream().map(this::mapToProjectData).collect(Collectors.toList()));
		data.setFlagNum(nextFlag);
		response.setData(data);

		log.info("3D_RESPONSE!!!!!!!! : {}", response);

		return ResponseEntity.ok(response);
	}

	// ThreeEntity를 GetProjectsResponse.GetProjectsData로 매핑하는 메서드
	private GetProjectsResponse.GetProjectsData mapToProjectData(ThreeEntity entity) {
		GetProjectsResponse.GetProjectsData data = new GetProjectsResponse.GetProjectsData();
		data.setProject_id(entity.getProjectId());
		data.setThumbnail(entity.getThumbnail());
		data.setTitle(entity.getTitle());
		data.setSrc(entity.getSrc());
		data.setRegDt(entity.getRegDate());
		return data;
	}
}
