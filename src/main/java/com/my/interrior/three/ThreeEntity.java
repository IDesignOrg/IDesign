package com.my.interrior.three;

import java.util.Date;
import java.util.List;

import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "three")
@Getter
@Setter
@ToString
public class ThreeEntity {

	@Id
	@Column(name = "project_id")
	private String projectId;

	@JoinColumn(name = "u_no")
	@ManyToOne
	private UserEntity userEntity;
	
	@Column(name = "mod_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modDate;
	
	@Column(name = "reg_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private Date regDate;

	@OneToMany(mappedBy = "threeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DataEntity> dataEntity; 
	
	
}
