package com.my.interrior.admin.page;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.my.interrior.admin.coupon.CouponMapRepository;
import com.my.interrior.admin.coupon.CouponRepository;
import com.my.interrior.admin.coupon.CouponService;
import com.my.interrior.client.csc.faq.FaqEntity;
import com.my.interrior.client.csc.faq.FaqRepository;
import com.my.interrior.client.csc.inquiry.InquiryAnswerDTO;
import com.my.interrior.client.csc.inquiry.InquiryAnswerEntity;
import com.my.interrior.client.csc.inquiry.InquiryAnswerRepository;
import com.my.interrior.client.csc.inquiry.InquiryEntity;
import com.my.interrior.client.csc.inquiry.InquiryListDTO;
import com.my.interrior.client.csc.inquiry.InquiryRepository;
import com.my.interrior.client.csc.notice.NoticeEntity;
import com.my.interrior.client.csc.notice.NoticeRepository;
import com.my.interrior.client.csc.recover.RecoveryEntity;
import com.my.interrior.client.csc.recover.RecoveryRepository;
import com.my.interrior.client.evaluation.ReviewCommentEntity;
import com.my.interrior.client.evaluation.ReviewCommentRepository;
import com.my.interrior.client.evaluation.ReviewEntity;
import com.my.interrior.client.evaluation.ReviewPhotoEntity;
import com.my.interrior.client.evaluation.ReviewPhotoRepository;
import com.my.interrior.client.evaluation.ReviewRepository;
import com.my.interrior.client.event.EventEntity;
import com.my.interrior.client.event.EventRepository;
import com.my.interrior.client.event.coupon.CouponEntity;
import com.my.interrior.client.event.coupon.CouponMapEntity;
import com.my.interrior.client.gcs.GCSFileDeleter;
import com.my.interrior.client.ordered.OrderedEntity;
import com.my.interrior.client.ordered.OrderedRepository;
import com.my.interrior.client.shop.ShopEntity;
import com.my.interrior.client.shop.ShopRepository;
import com.my.interrior.client.shop.ShopReviewRepository;
import com.my.interrior.client.user.UserEntity;
import com.my.interrior.client.user.UserRepository;
import com.my.interrior.client.user.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class AdminPageService {

	@Autowired
	private NoticeRepository noticeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShopRepository shopRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private ReviewCommentRepository reviewCommentRepository;

	@Autowired
	private ShopReviewRepository shopReviewRepository;

	@Autowired
	private NoticeRepository noticerepository;

	@Autowired
	private RecoveryRepository recoveryRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CouponMapRepository couponMap;

	@Autowired
	private CouponService couponService;

	@Autowired
	private OrderedRepository orderedRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private FaqRepository faqRepository;

	@Autowired
	private InquiryRepository inquiryRepository;

	@Autowired
	private InquiryAnswerRepository inquiryAnswerRepository;
	
	@Autowired
	private ReviewPhotoRepository reviewPhotoRepository;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private GCSFileDeleter gcsFileDelter;

	// 유저 카운트
	public long getUserCount() {
		return userRepository.count();
	}

	// 상점 카운트
	public long getShopCount() {
		return shopRepository.count();
	}

	// 리뷰 카운트
	public long getReviewCount() {
		return reviewRepository.count();
	}

	// 가장 높은 조회수를 가진 상점
	public Optional<ShopEntity> getMostViewedShop() {
		return shopRepository.findTopByOrderByShopHitDesc();
	}

	public Optional<ShopEntity> getMostSelledShop() {
		return shopRepository.findTopByOrderByShopSellDesc();
	}

	// 유저 페이지
	public Page<UserWithPostAndCommentCount> findAllUsersWithCounts(Pageable pageable) {
		List<UserEntity> users = userRepository.findAll();
		List<UserWithPostAndCommentCount> userCounts = new ArrayList<>();

		for (UserEntity user : users) {
			int postCount = reviewRepository.countByUser(user);
			int commentCount = shopReviewRepository.countByUser(user);
			UserWithPostAndCommentCount dto = new UserWithPostAndCommentCount(user, postCount, commentCount);
			userCounts.add(dto);
		}

		// 페이징 처리
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), userCounts.size());
		List<UserWithPostAndCommentCount> pagedList = userCounts.subList(start, end);

		return new PageImpl<>(pagedList, pageable, userCounts.size());
	}

	// 리뷰 페이지
	public Page<ReviewAndCommentDTO> findAllReviewAndCounts(Pageable pageable) {
		List<ReviewEntity> reviews = reviewRepository.findAll();
		List<ReviewAndCommentDTO> reviewCounts = new ArrayList<>();

		for (ReviewEntity review : reviews) {
			int commentCount = reviewCommentRepository.countByReviewEntity(review);
			ReviewAndCommentDTO dto = new ReviewAndCommentDTO(review, commentCount);
			reviewCounts.add(dto);
		}
		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), reviewCounts.size());
		List<ReviewAndCommentDTO> pagedList = reviewCounts.subList(start, end);
		return new PageImpl<>(pagedList, pageable, reviewCounts.size());
	}
	//어드민페이지 리뷰 삭제하기
	@Transactional
    public void deleteReviewById(Long rNo) {
        // 리뷰에 연결된 댓글을 찾고 해당 댓글들 전체 삭제
        List<ReviewCommentEntity> comments = reviewCommentRepository.findByReviewEntity_RNo(rNo);
        if (!comments.isEmpty()) {
        	reviewCommentRepository.deleteAll(comments); // 댓글 삭제
        }

        // 리뷰에 연결된 사진을 찾고 gcs로 사진부터 지운뒤 해당 사진 전체 삭제
        List<ReviewPhotoEntity> photos = reviewPhotoRepository.findByReview_RNo(rNo);
        if (!photos.isEmpty()) {
            for (ReviewPhotoEntity photo : photos) {
            	gcsFileDelter.deleteFile(photo.getRpPhoto()); // GCS에서 파일 삭제
            }
            reviewPhotoRepository.deleteAll(photos); // 사진 삭제
        }

        // 마지막으로 리뷰 삭제
        if (reviewRepository.existsById(rNo)) {
            reviewRepository.deleteById(rNo);
        } else {
            throw new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다.");
        }
    }

	// 유저 검색하기
	public List<UserWithPostAndCommentCount> searchUsers(String searchType, String searchInput, LocalDate startDate,
			LocalDate endDate, String orderType) {
		List<UserEntity> users;

		// 검색 조건에 따라 사용자 목록 필터링
		switch (searchType) {
		case "name":
			users = userRepository.findByUNameContainingIgnoreCaseAndURegisterBetween(searchInput, startDate, endDate);
			break;
		case "nickname":
			users = userRepository.findByUIdContainingIgnoreCaseAndURegisterBetween(searchInput, startDate, endDate);
			break;
		case "email":
			users = userRepository.findByUMailContainingIgnoreCaseAndURegisterBetween(searchInput, startDate, endDate);
			break;
		default:
			users = userRepository.findAllByURegisterBetween(startDate, endDate);
			break;
		}

// 사용자 목록을 UserWithPostAndCommentCount로 변환
		List<UserWithPostAndCommentCount> userCounts = users.stream().map(user -> {
			int postCount = reviewRepository.countByUser(user);
			int commentCount = shopReviewRepository.countByUser(user);
			return new UserWithPostAndCommentCount(user, postCount, commentCount);
		}).collect(Collectors.toList());

// 정렬 방식에 따른 정렬
		if (orderType.equals("postCountDesc")) {
			userCounts.sort((u1, u2) -> Integer.compare(u2.getPostCount(), u1.getPostCount()));
		} else if (orderType.equals("commentCountDesc")) {
			userCounts.sort((u1, u2) -> Integer.compare(u2.getCommentCount(), u1.getCommentCount()));
		}

		return userCounts;
	}

	// 공지사항 가져오기
	public Page<NoticeEntity> getAllNotice(Pageable pageable) {
		return noticerepository.findAll(pageable);
	}

	// 이벤트 리스트
	public Page<EventEntity> getAllEvent(Pageable pageable) {
		return eventRepository.findAll(pageable);
	}
	//이벤트 삭제
	@Transactional
    public void deleteEvent(Long eventNo) throws Exception {
        if (eventRepository.existsById(eventNo)) {
            eventRepository.deleteById(eventNo);
        } else {
            throw new Exception("이벤트를 찾을 수 없습니다.");
        }
    }

	// 이벤트 제목 검색
	public Page<EventEntity> searchEventsByTitle(String title, Pageable pageable) {
		return eventRepository.findByEventTitleContaining(title, pageable);
	}

	// 이벤트 쿠폰 이름 검색
	public Page<EventEntity> searchEventsByCouponName(String couponName, Pageable pageable) {
		return eventRepository.findByCoupon_CouponNameContaining(couponName, pageable);
	}

	// 자주묻는 질문 가져오기
	public Page<FaqEntity> getAllFaq(Pageable pageable) {
		return faqRepository.findAll(pageable);
	}

	// 문의하기 전체 불러오기
	public Page<InquiryListDTO> getAllInquiries(Pageable pageable) {
		// InquiryEntity 페이지를 조회
		Page<InquiryEntity> inquiries = inquiryRepository.findAll(pageable);

		// InquiryEntity를 InquiryListDTO로 변환, 답변 여부를 확인
		List<InquiryListDTO> inquiryDTOs = inquiries.stream().map(inquiry -> {
			// 답변 여부를 확인 (답변이 있는지 확인하는 메서드를 통해)
			boolean hasAnswer = inquiryAnswerRepository.existsByInquiry(inquiry);
			return new InquiryListDTO(inquiry, hasAnswer); // InquiryListDTO로 객체 생성
		}).collect(Collectors.toList());

		// Page 객체로 반환
		return new PageImpl<>(inquiryDTOs, pageable, inquiries.getTotalElements());
	}

	// 문의하기 질문 불러오기
	public InquiryEntity getInquiryById(Long inqNo) {
		return inquiryRepository.findByinqNo(inqNo);
	}

	// 문의하기 답변 불러오기
	public InquiryAnswerEntity getInquiryAnswerById(Long inqNo) {
		return inquiryAnswerRepository.findByInquiryInqNo(inqNo);
	}

	// 문의하기 답변 저장
	public void saveInquiryAnswer(Long inqNo, String answerContent) {
		InquiryEntity inquiryEntity = getInquiryById(inqNo);

		InquiryAnswerEntity answerEntity = new InquiryAnswerEntity();
		answerEntity.setAnsContent(answerContent);
		answerEntity.setAnsRegisteredDate(LocalDate.now());
		answerEntity.setInquiry(inquiryEntity);

		// 답변 작성자 정보 설정 (현재 로그인된 사용자)
		String userId = (String) session.getAttribute("UId");

		UserEntity userEntity = userRepository.findByUId(userId);
		answerEntity.setUserEntity(userEntity);

		inquiryAnswerRepository.save(answerEntity);
	}

	// 공지사항 삭
	@Transactional
	public void deleteNotice(Long notNo) {
		noticeRepository.deleteById(notNo);
	}

	// 복구하기가져오기
	public Page<RecoveryEntity> getAllRecovery(Pageable pageable) {
		return recoveryRepository.findAll(pageable);
	}

	// 복구 요청을 처리하는 메서드
	public void processRecovery(Long recoverNo) {
		// 복구 요청을 데이터베이스에서 가져오기
		RecoveryEntity recovery = recoveryRepository.findById(recoverNo)
				.orElseThrow(() -> new IllegalArgumentException("해당 복구 요청을 찾을 수 없습니다: " + recoverNo));

		// 복구 처리 (여기서는 단순히 상태와 처리 날짜를 업데이트)
		recovery.setStatus("COMPLETED");
		recovery.setProcessedDate(LocalDate.now());

		UserEntity user = recovery.getUser();
		user.setUDeactivated(false);

		userRepository.save(user);

		// 변경된 복구 요청을 저장
		recoveryRepository.save(recovery);
	}

	// 복구하기 검색
	public List<RecoveryEntity> searchRecoveryRequests(String searchType, String searchInput, LocalDate startDate,
			LocalDate endDate) {
		if (startDate == null)
			startDate = LocalDate.of(1900, 1, 1);
		if (endDate == null)
			endDate = LocalDate.now();

		switch (searchType) {
		case "userId":
			return recoveryRepository.findByUser_UIdContainingIgnoreCaseAndRequestDateBetween(searchInput, startDate,
					endDate);
		case "name":
			return recoveryRepository.findByUser_UNameContainingIgnoreCaseAndRequestDateBetween(searchInput, startDate,
					endDate);
		case "email":
			return recoveryRepository.findByUser_UMailContainingIgnoreCaseAndRequestDateBetween(searchInput, startDate,
					endDate);
		default:
			return new ArrayList<>();
		}
	}

	public List<CouponEntity> getAllModalCoupons() {
		return couponRepository.findAll();
	}

	public Page<CouponEntity> getAllCoupons(Pageable pageable) {
		return couponRepository.findAll(pageable);
	}

	public Page<CouponMapEntity> getAllUserCoupons(Pageable pageable) {
		return couponMap.findAll(pageable);
	}

	public Optional<CouponEntity> findCouponById(Long couponNo) {
		return couponRepository.findById(couponNo);
	}

	public void deleteCouponById(Long couponMapId) {
		if (couponMap.existsById(couponMapId)) {
			couponMap.deleteById(couponMapId);
		} else {
			throw new IllegalArgumentException("해당 쿠폰이 존재하지 않습니다.");
		}
	}

	public void updateCouponState(Long couponNo, String state) {
		CouponEntity coupon = couponService.findCouponById(couponNo);
		coupon.setCouponState(state);
		couponRepository.save(coupon);
	}

	@Transactional
	public void recoveryUser(Long userUNo) {
		UserEntity user = userRepository.findByUNo(userUNo);

		user.setUDeactivated(false);
		userRepository.save(user);

	}

	// 어드민 샵 리스트
	public Page<ShopListAndOrderedDTO> getAllShopsAndCounts(Pageable pageable) {
		Page<ShopEntity> shops = shopRepository.findAll(pageable);
		List<ShopListAndOrderedDTO> shopCounts = new ArrayList<>();

		for (ShopEntity shop : shops) {
			int orderedCount = orderedRepository.countByShopNo(shop.getShopNo());
			ShopListAndOrderedDTO dto = new ShopListAndOrderedDTO(shop, orderedCount);
			shopCounts.add(dto);
		}

		return new PageImpl<>(shopCounts, pageable, shops.getTotalElements());
	}

	// 어드민 샵 검색
	public Page<ShopListAndOrderedDTO> searchShops(String shopTitle, String shopCategory, Integer minPrice,
			Integer maxPrice, Pageable pageable) {
		List<ShopEntity> shops = shopRepository.findByShopTitleContainingAndShopCategoryContaining(shopTitle,
				shopCategory);

		BigDecimal minPriceBigDecimal = minPrice != null ? BigDecimal.valueOf(minPrice) : BigDecimal.ZERO;
		BigDecimal maxPriceBigDecimal = maxPrice != null ? BigDecimal.valueOf(maxPrice)
				: BigDecimal.valueOf(Long.MAX_VALUE);

		List<ShopEntity> filteredShops = shops.stream().filter(shop -> {
			BigDecimal price = new BigDecimal(shop.getShopPrice());
			return price.compareTo(minPriceBigDecimal) >= 0 && price.compareTo(maxPriceBigDecimal) <= 0;
		}).collect(Collectors.toList());

		List<ShopListAndOrderedDTO> shopCounts = filteredShops.stream().map(shop -> {
			int orderedCount = orderedRepository.countByShopNo(shop.getShopNo());
			return new ShopListAndOrderedDTO(shop, orderedCount);
		}).collect(Collectors.toList());

		int start = (int) pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), filteredShops.size());
		List<ShopListAndOrderedDTO> pagedShopCounts = shopCounts.subList(start, end);

		return new PageImpl<>(pagedShopCounts, pageable, shopCounts.size());
	}

	// 구매내역
	public Page<OrderedEntity> getAllOrdered(Pageable pageable) {
		return orderedRepository.findAll(pageable);
	}

	// 상점 비활성화 기능
	@Transactional
	public void toggleShopActivation(Long shopNo, boolean isDeactivated) {
		System.out.println("isDeactivated : " + isDeactivated);
		ShopEntity shop = shopRepository.findById(shopNo)
				.orElseThrow(() -> new IllegalArgumentException("해당 상점을 찾을 수 없습니다: " + shopNo));

		// 비활성화 처리
		shop.setSDeactivated(isDeactivated);
		shopRepository.save(shop);
	}

	// ordered 배송상태 변환
	public void updateShipmentState(Long orderedNo, String shipmentState) throws Exception {
		OrderedEntity order = orderedRepository.findById(orderedNo).orElseThrow(() -> new Exception("주문을 찾을 수 없습니다."));

		order.setShipmentState(shipmentState);

		orderedRepository.save(order);
	}

	// FAQ 수정
	public boolean updateFaq(Long faqNo, FaqEntity faqData) {
		Optional<FaqEntity> existingFaqOpt = faqRepository.findById(faqNo);
		if (existingFaqOpt.isPresent()) {
			FaqEntity existingFaq = existingFaqOpt.get();
			existingFaq.setFaqTitle(faqData.getFaqTitle());
			existingFaq.setFaqContent(faqData.getFaqContent());
			existingFaq.setFaqCategory(faqData.getFaqCategory());
			faqRepository.save(existingFaq);
			return true;
		}
		return false;
	}

	// 특정 FAQ 조회
	public FaqEntity getFaqById(Long faqNo) {
		Optional<FaqEntity> faq = faqRepository.findById(faqNo);
		return faq.orElse(null); // FAQ가 없으면 null 반환
	}

	// FAQ 삭제
	public boolean deleteFaq(Long faqNo) {
		if (faqRepository.existsById(faqNo)) {
			faqRepository.deleteById(faqNo);
			return true;
		}
		return false;
	}
}
