package com.my.interrior.client.mypage;

import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MypageController {

	@Autowired
	private UserRepository userRepository; // JpaRepository(save, deleteBy)

	@Autowired
	private MypageService mypageService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@GetMapping("/mypage/mypage")
	public String myPage(HttpSession session, Model model) {

		String UId = (String) session.getAttribute("UId");

		UserEntity user = mypageService.getUserInfo(UId);

		model.addAttribute("userInfo", user);

		return "client/mypage/mypage";
	}

	@GetMapping("mypage/check_password")
	public String getMethodName() {
		return "client/mypage/check_password";
	}

	@PostMapping("mypage/check_password")
	public String check_password(@RequestParam("password") String password, HttpSession session,
			RedirectAttributes redirectAttributes) {
		String UId = (String) session.getAttribute("UId");
		UserEntity user = mypageService.getUserInfo(UId);

		if (user != null && passwordEncoder.matches(password, user.getUPw())) {
			return "redirect:/mypage/mypageUpdate";
		} else {
			redirectAttributes.addFlashAttribute("error", "Invalid password");
			return "redirect:/mypage/check-password";
		}
	}

	@GetMapping("mypage/mypageUpdate")
	public String mypageUpdateG(HttpSession session, Model model) {
		String UId = (String) session.getAttribute("UId");
		UserEntity user = mypageService.getUserInfo(UId);
		model.addAttribute("userInfo", user);
		return "client/mypage/mypageUpdate";
	}

	@PostMapping("/UpdateForUser")
	public String mypageUpdate(@RequestParam("file") MultipartFile file, @RequestParam("UName") String UName,
			@RequestParam("UBirth") String UBirth, @RequestParam("UMail") String UMail,
			@RequestParam("UTel") String UTel, HttpSession session, Model model) throws IOException {

		mypageService.updateUser(file, UName, UBirth, UMail, UTel);
		String UId = (String) session.getAttribute("UId");
		UserEntity user = mypageService.getUserInfo(UId);

		model.addAttribute("userInfo", user);

		return "client/mypage/mypage";
	}

	@GetMapping("/mypage/mypageUpdatePassword")
	public String mypageUpdatePassword() {
		return "client/mypage/mypageUpdatePassword";
	}

	@PostMapping("/updatePassword")
    public String updatePassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmNewPassword") String confirmNewPassword,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        String UId = (String) session.getAttribute("UId");
        UserEntity user = userService.findByUId(UId);

        //정규식 패턴
        String passwordPattern = "^(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$";
        // 현재 비밀번호가 일치하지 않는 경우
        if (!userService.checkPasswordMatch(currentPassword, user.getUPw())) {
            redirectAttributes.addFlashAttribute("errorMessage", "현재 비밀번호를 확인해주세요.");
            return "redirect:/mypage/mypageUpdatePassword";
        }

        // 새 비밀번호와 확인 비밀번호가 일치하지 않는 경우
        if (!newPassword.equals(confirmNewPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/mypageUpdatePassword";
        }
        //유효성 검사
        if (!newPassword.matches(passwordPattern)) {
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호는 숫자와 특수문자를 포함하여 8자 이상이어야 합니다.");
            return "redirect:/mypage/mypageUpdatePassword";
        }

        // 비밀번호 업데이트
        userService.updatePassword(UId, newPassword);
        redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/mypage/mypage";
    }

}
