package com.my.interrior.client.evaluation;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.Multipart;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {
	
	private static final int PAGE_SIZE = 10;
	
	@Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;

    //후기 작성 만들기
	@GetMapping("/review_write")
	public String review(Model model, HttpSession session) {
		String userid = (String)session.getAttribute("UID");
		model.addAttribute("userid", userid);
		return "client/review/review_write"; 
	}
	
	//후기 작성
	@PostMapping("/review_write")
    public String createReview(
            @RequestParam("title") String title,
            @RequestParam("category") String category,
            @RequestParam("content") String content,
            @RequestParam("starRating") String starRating,
            @RequestParam("files") MultipartFile[] files,
            @RequestParam("mainPhoto") MultipartFile mainPhoto,
            Model model) throws IOException {
        
        reviewService.uploadFileAndCreateReview(title, category, content, starRating, files, mainPhoto);

        return "redirect:/review_write";
    }
	
	//후기 페이지
	@GetMapping("/auth/evaluation")
	public String allReviews(Model model, Pageable pageable) {
	    Page<ReviewEntity> reviews = reviewService.getAllReviews(PageRequest.of(pageable.getPageNumber(), PAGE_SIZE));
	    
	    model.addAttribute("reviews", reviews.getContent());
	    model.addAttribute("currentPage", pageable.getPageNumber());
	    model.addAttribute("totalPages", reviews.getTotalPages());
	    for (ReviewEntity review : reviews.getContent()) {
	        List<ReviewPhotoEntity> reviewPhotos = reviewService.getReviewPhotosByReviewNo(review.getRNo());
	        System.out.println("사진은 : " + reviewPhotos);
	        model.addAttribute("reviewPhotos_" + review.getRNo(), reviewPhotos);
	    }
	    return "client/review/reviewList";    
	}
	
	//후기 상세페이지
	@GetMapping("/auth/evaluation/{rNo}")
	public String reviewDetail(@PathVariable("rNo") Long rNo, Model model) {
		//reviewService.updateHits(rNo);
		Optional<ReviewEntity> review = reviewService.getReviewById(rNo);
		if (review.isPresent()) {		
			model.addAttribute("review", review.get());
			System.out.println("review 는" + review);
		} else {
			return "error";
		}
		List<ReviewPhotoEntity> reviewPhoto = reviewService.getPhotosByReviewId(rNo);
		if(review.isPresent()) {
			model.addAttribute("reviewPhoto", reviewPhoto);
			System.out.println("reviewPhoto : " + reviewPhoto);
		}else {
			return "error";
		}
		
		
		return "client/review/reviewDetail";
	}
	
}
