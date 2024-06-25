package com.my.interrior.client.csc.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<NoticeEntity, Long>{
	
	NoticeEntity findBynotNo(Long notNo);
}
