package com.my.interrior.client.csc.inquiry;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface InquiryRepository extends JpaRepository<InquiryEntity, Long>{

	@Query("SELECT DISTINCT i.inqCategory from inquiry i")
	List<String> findAllCategory();
	Page<InquiryEntity> findByinqCategory(String category, Pageable pageable);
	InquiryEntity findByinqNo(Long inqNo);
}
