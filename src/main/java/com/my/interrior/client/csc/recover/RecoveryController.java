package com.my.interrior.client.csc.recover;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.my.interrior.client.user.UserDTO;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserService;


@Controller
public class RecoveryController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private RecoveryService recoveryService;
	
	@GetMapping("/board/recoverLogin")
	public String recoverLogin() {
		return "client/csc/recoverLogin";
	}

	@PostMapping("/board/recoverLogin")
	public String login(@ModelAttribute UserDTO userDTO, Model model, RedirectAttributes redirectAttributes) throws Exception {
	    try {
	        if (userDTO.getUId() == null || userDTO.getUPw() == null)
	            return "redirect:/board/recoverLogin";

	        String UId = userDTO.getUId();
	        String UPw = userDTO.getUPw();
	        UserEntity user = userService.checkLogin(UId);
	        System.out.println("user: " + user);

	        if (user != null && passwordEncoder.matches(UPw, user.getUPw())) {

	            // 리다이렉트 시 엔티티를 그대로 전달
	            redirectAttributes.addFlashAttribute("user", user);

	            // 복구 신청 페이지로 리다이렉트
	            return "redirect:/board/recoverWrite";
	        } else {
	            model.addAttribute("loginError", "입력하신 정보를 확인하세요.");
	            return "/board/recoverLogin";
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        return "error";
	    }
	}

	@GetMapping("/board/recoverWrite")
	public String showRecoveryForm(@ModelAttribute("user") UserEntity user, Model model) {
	    if (user == null) {
	        // 만약 유저 정보가 없다면 로그인 페이지로 리다이렉트
	        return "redirect:/board/recoverLogin";
	    }

	    // 유저 정보를 모델에 추가하여 뷰에 전달
	    model.addAttribute("user", user);
	    model.addAttribute("requestDate", LocalDate.now()); // 현재 날짜를 모델에 추가
	    return "client/csc/recoverWrite";
	}
	
	@PostMapping("/board/submitRecovery")
	public String submitRecovery(@RequestParam("userNO") Long userNO,
	                             @RequestParam("requestDate") LocalDate requestDate,
	                             @RequestParam("reason") String reason,
	                             Model model) {
	    try {
	        // 복구 엔티티 생성 및 데이터 저장
	    	System.out.println("일단 들어옴");
	        RecoveryEntity recovery = new RecoveryEntity();
	        UserEntity user = userService.findById(userNO);
	        if (user == null) {
	            model.addAttribute("error", "유효하지 않은 사용자입니다.");
	            return "recoverWrite";
	        }
	        
	        recovery.setUser(user);
	        recovery.setRequestDate(requestDate);
	        recovery.setReason(reason);
	        recovery.setStatus("PENDING");

	        recoveryService.saveRecoveryRequest(recovery);

	        // 성공 메시지 추가
	        model.addAttribute("message", "복구 신청이 성공적으로 제출되었습니다.");
	        return "redirect:/"; // 복구 성공 페이지로 이동

	    } catch (Exception e) {
	        e.printStackTrace();
	        model.addAttribute("error", "복구 신청 중 오류가 발생했습니다. 다시 시도해 주세요.");
	        return "recoverWrite"; // 오류 발생 시 다시 폼 페이지로
	    }
	}

	
}
