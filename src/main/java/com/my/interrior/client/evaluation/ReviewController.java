package com.my.interrior.client.evaluation;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ReviewController {
	
	@Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

	@GetMapping("/auth/review_write")
	public String review() {
		return "client/review/review_write"; 
	}
	
	@PostMapping("/auth/review_write")
	public String createReview(
			@RequestParam("title") String title,
			@RequestParam("file") MultipartFile file,
			@RequestParam("category") String category,
			Model model) {
		try {
            String imageUrl = reviewService.uploadFile(file);

            ReviewEntity review = new ReviewEntity();
            review.setRTitle(title);
            review.setRMainphoto(imageUrl);
            review.setRCategory(category);

            reviewRepository.save(review);

            model.addAttribute("message", "Review created successfully!");

        } catch (IOException e) {
            model.addAttribute("message", "Failed to upload image!");
            e.printStackTrace();
        }

        return "redirect:/auth/review_write";
    }
	
}
