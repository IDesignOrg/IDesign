package com.my.interrior.client.csc.notice;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends PagingAndSortingRepository<NoticeEntity, Long>{
}
