package com.my.interrior.three;

import java.time.LocalDateTime;
import java.util.List;

import com.my.interrior.client.user.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
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
	@Column(unique = true, nullable = false)
	private String projectId;

	@JoinColumn(name = "u_no")
	@ManyToOne
	private UserEntity userEntity;
	
	@Column(name = "mod_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime modDate;
	
	@Column(name = "reg_dt")
	@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime regDate;

	@OneToMany(mappedBy = "threeEntity", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DataEntity> dataEntity; 
	
	//엔티티 만들 때 자동으로 생성시켜주는 어노테이션
	@PrePersist
	protected void onCreate() {
		regDate = LocalDateTime.now();
		modDate = LocalDateTime.now();
	}
	
	//엔티티 수정할 때 자동으로 수정시켜주는 어노테이션
	@PreUpdate
	protected void onUpdate() {
		modDate = LocalDateTime.now();
	}
}
