package com.my.interrior.client.evaluation;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ReviewController {
	
	private static final int PAGE_SIZE = 10;
	
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
	
	@GetMapping("/auth/evaluation")
	public String allReviews(Model model, Pageable pageable) {
	    Page<ReviewEntity> reviews = reviewService.getAllReviews(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));

	    model.addAttribute("reviews", reviews.getContent());
	    model.addAttribute("currentPage", pageable.getPageNumber());
	    model.addAttribute("totalPages", reviews.getTotalPages());
	    return "client/review/reviewList";    
	}
	
	@GetMapping("/auth/evaluation/{rNo}")
	public String reviewDetail(@PathVariable("rNo") Long rNo, Model model) {
		Optional<ReviewEntity> review = reviewService.getReviewById(rNo);
		if (review.isPresent()) {		
			model.addAttribute("review", review.get());
			System.out.println("review 는" + review);
		} else {
			return "error";
		}
		
		return "client/review/reviewDetail";
	}
}
