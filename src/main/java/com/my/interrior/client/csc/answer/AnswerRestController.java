package com.my.interrior.client.csc.answer;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.my.interrior.client.csc.inquiry.InquiryEntity;
import com.my.interrior.client.csc.inquiry.InquiryRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AnswerRestController {

	private final AnswerRepository answerRepository;
	private final AnswerService answerService;
	private final InquiryRepository inquiryRepository;

	@PostMapping("/board/answer/{inqNo}")
	public ResponseEntity<List<AnswerEntity>> answeredTheQuestion(@RequestBody AnswerEntity answerEntity,
			@PathVariable("inqNo") Long inqNo) {
		try {
			InquiryEntity inq = inquiryRepository.findByinqNo(inqNo);
			answerEntity.setInquiry(inq);

			answerRepository.save(answerEntity);

			// 저장 후 다시 조회하여 반환
			List<AnswerEntity> savedAnswer = answerService.findInquiry(inqNo);
			System.out.println("answer의 값은: " + savedAnswer);

			return ResponseEntity.ok(savedAnswer);
		} catch (Exception e) {
			// 예외 발생 시 처리
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@GetMapping("/board/answer/{inqNo}")
	public ResponseEntity<List<AnswerEntity>> getAnswersByInquiry(@PathVariable("inqNo") Long inqNo) {
		try {
			List<AnswerEntity> answers = answerService.findInquiry(inqNo);
			return ResponseEntity.ok(answers);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

}
