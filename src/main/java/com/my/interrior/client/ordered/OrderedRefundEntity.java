package com.my.interrior.client.ordered;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity(name = "ordered_refund")
@Getter
@Setter
@ToString
public class OrderedRefundEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long refundNo;
	
	@ManyToOne
    @JoinColumn(name = "ordered_no", nullable = false)
    private OrderedEntity orderedEntity;

    // 환불 사유 컬럼 추가
    @Column(name = "refund_reason", nullable = false, length = 255)
    private String refundReason;
    
    //환불 신청자
    @Column(name = "refund_user", nullable = false)
    private String refundUser;
	

}
