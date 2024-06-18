package com.my.interrior.client.evaluation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ReviewController {
	
	@Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

	@GetMapping("/review_write")
	public String review() {
		return "client/review/review_write"; 
	}
	
	@PostMapping("/review_write")
    public String createReview(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            @RequestParam("starRating") String starRating,
            @RequestParam("files") MultipartFile[] files,
            Model model) throws IOException {
        
        reviewService.uploadFileAndCreateReview(title, category, content, starRating, files);

        return "redirect:/review_write";
    }
	
}
