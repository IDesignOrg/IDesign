package com.my.interrior.client.csc.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends JpaRepository<AnswerEntity, Long>{

	List<AnswerEntity> findByInquiry_inqNo(Long inqNo);
}
