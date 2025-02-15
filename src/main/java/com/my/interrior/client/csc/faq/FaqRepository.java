package com.my.interrior.client.csc.faq;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqRepository extends JpaRepository<FaqEntity, Long>{
	
	FaqEntity findByfaqNo(Long faqNo);
	
	@Query("SELECT DISTINCT f.faqCategory from faq f")
	List<String> findAllfaqCategory();
	
	Page<FaqEntity> findByfaqCategory(String category, PageRequest Pageable);
	
//	@Query("SELECT f FROM faq f WHERE f.faqTitle LIKE %:keyword%")
//	Page<FaqEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
	
	Page<FaqEntity> findByfaqTitleContaining(String keyword, Pageable pageable);



}
