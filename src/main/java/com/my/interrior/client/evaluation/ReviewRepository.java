package com.my.interrior.client.evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface ReviewRepository extends JpaRepository<ReviewEntity, Long>{
	 	//@Modifying
	    //@Query("update review r set r.RViews = r.RViews + 1 where r.rNo = :rNo")
	    //int updateHits(Long rNo);
}
