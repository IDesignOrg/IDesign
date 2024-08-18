package com.my.interrior.client.event;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.my.interrior.admin.coupon.CouponRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class EventController {

	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private CouponRepository couponRepository;
	
	@Autowired
	private EventService eventService;

	@GetMapping("/board/event")
	public String showEventPage(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {
		Page<EventEntity> eventPage = eventRepository.findAll(PageRequest.of(page, 5));
		model.addAttribute("events", eventPage);
		return "client/event";
	}
	
	// 이벤트 상세 페이지
	@GetMapping("/board/eventDetail")
	public String showEventDetailPage(Model model, @RequestParam(name = "eventNo") Long eventNo) {
		Optional<EventEntity> eventOptional = eventRepository.findById(eventNo);
		if (eventOptional.isPresent()) {
			EventEntity event = eventOptional.get();
			model.addAttribute("event", event);
			return "client/eventDetail";
		} else {
			// 이벤트를 찾지 못한 경우 처리
			return "redirect:/auth/event";
		}
	}
	@GetMapping("/board/event/write")
	public String goToEventWrite(Model model) {
		model.addAttribute("coupons", couponRepository.findAll());
		return "client/eventWrite";
	}
	@PostMapping("/board/event/write")
	public String eventWrite(
            @RequestParam("eventTitle") String eventTitle,
            @RequestParam("eventContent") String eventContent,
            @RequestParam("eventImg") MultipartFile eventImg,
            @RequestParam("couponNo") Long couponNo) throws IOException {
		
		
		eventService.saveEvent(eventTitle, eventContent, eventImg, couponNo);
		//여기 다시설정 하셈
		return "redirect:/admin/page/adminEvent";
	}
}
