package com.my.interrior.client.ordered;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my.interrior.client.user.UserEntity;

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

@Entity(name = "ordered")
@Setter
@Getter
@ToString
public class OrderedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderedNo;
	private Long orderedNumber;
	private String orderedState;
	private String shipmentState;
	private LocalDate orderedDate;
	@Column(name = "shop_no")
	private Long shopNo;
	
	@JoinColumn(name = "u_no")
	@ManyToOne
	@JsonIgnore
	private UserEntity userEntity;
}
