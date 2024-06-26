package com.my.interrior.client.csc.answer;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;
	
	public List<AnswerEntity> findInquiry(Long inqNo) {
		return answerRepository.findByInquiry_inqNo(inqNo);
	}
	
}
