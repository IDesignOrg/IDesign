package com.my.interrior.three;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
public class ThreeController {
	
	private final UserRepository userRepository;
	
	@GetMapping("/three/dashboard")
	public String goToThreeDashBoard(Model model, HttpSession session) throws Exception {

		String userId = (String) session.getAttribute("UId");

		model.addAttribute("userId", userId);
		return "dashboard/dashboard";
	}

	@GetMapping("/three/design")
	public String goToThreeDesign(Model model, HttpSession session) throws Exception {

		String userId = (String) session.getAttribute("UId");
		UserEntity user = userRepository.findByUId(userId);
		long userNo = user.getUNo();

		session.setAttribute("userNo", userNo);
		return "dist/three";

	}
}
