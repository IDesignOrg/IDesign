package com.my.interrior.three;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "data")
@Getter
@Setter
@ToString
public class DataEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "oid")
	private Long oid;
	
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@ManyToOne
	@JoinColumn(name = "parent_oid")
	private DataEntity parent;
	
	@OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DataEntity> children;
	
	@OneToMany(mappedBy = "dataEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PointEntity> points;
	
	@Column(name = "rotation", nullable = false)
	private Double rotation;
	
	@Column(name = "angle", nullable = false)
	private Double angle;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ThreeEntity threeEntity;
	
}
