package com.my.interrior.three;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
	private Long id;

	@Column(name = "oid")
	private String oid;
	
	
	@Column(name = "type", nullable = false)
	private String type;
	
	@ManyToOne
	@JoinColumn(name = "project_id", nullable = false)
	private ThreeEntity threeEntity;
	
	@OneToMany(mappedBy = "data")
	private List<PointEntity> points;
	
	@Column(name = "rotation", nullable = false)
	private Double rotation;

	@ElementCollection
    @Column(name = "child_oid")
    private List<String> children;
	
	@Column(name = "parent", nullable = true)
    private String parent;
	
	
//	@Column(name = "angle", nullable = false)
//	private Double angle;
	
	
}
