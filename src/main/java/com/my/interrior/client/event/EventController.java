package com.my.interrior.client.event;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class EventController {

	@Autowired
	private EventRepository eventRepository;

	@GetMapping("/auth/event")
	public String showEventPage(Model model, @RequestParam(defaultValue = "0", name = "page") int page) {
		Page<EventEntity> eventPage = eventRepository.findAll(PageRequest.of(page, 5));
		model.addAttribute("events", eventPage);
		return "client/event";
	}

	// 이벤트 상세 페이지
	@GetMapping("/auth/eventDetail")
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
}
