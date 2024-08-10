package com.my.interrior.three;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "point")
@Getter
@Setter
@ToString
public class PointEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "x")
	private Double x;
	
	@Column(name = "y")
	private Double y;
	
	@Column(name = "z")
	private Double z;
	
	@ManyToOne
	@JoinColumn(name = "oid", nullable = false)
	private DataEntity dataEntity;
}
