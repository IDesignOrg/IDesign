package com.my.interrior.client.csc.notice;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long>{
	
	NoticeEntity findBynotNo(Long notNo);
	Page<NoticeEntity> findBynotTitleContaining(String keyword, Pageable pageable);
}
