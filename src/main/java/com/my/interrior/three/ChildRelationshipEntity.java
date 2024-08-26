package com.my.interrior.three;

import com.my.interrior.three.SaveProjectRequest.DataRequest;

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
@Table(name = "child_relationship")
@Getter
@Setter
@ToString
public class ChildRelationshipEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "parent_id")
	private DataEntity parent;

	@ManyToOne
	@JoinColumn(name = "child_id")
	private DataEntity child;
	
}
