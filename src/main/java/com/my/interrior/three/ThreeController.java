package com.my.interrior.three;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ThreeController {
	
	private final UserRepository userRepository;
	
	@GetMapping("/three/dashboard")
	public String goToThreeDashBoard(Model model, HttpSession session) throws Exception {

		String userId = (String) session.getAttribute("UId");

		model.addAttribute("userId", userId);
		return "dashboard";
	}

	@GetMapping("/three")
	public String goToThreeDesign(@RequestParam("project_id") String projectId, Model model, HttpSession session) throws Exception {

		String userId = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(userId);
		long userNo = user.getUNo();

		session.setAttribute("userNo", userNo);
		session.setAttribute("projectId", projectId);
		return "three";

	}
}
