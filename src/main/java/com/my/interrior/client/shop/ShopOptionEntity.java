package com.my.interrior.client.shop;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity(name="shop_option")
@Getter
@Setter
public class ShopOptionEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long shopOptionNo;
	
	@Column(name="shop_option_name")
	private String shopOptionName;

	@ManyToOne
	@JoinColumn(name="shop_no")
	private ShopEntity shopEntity;
	
	@OneToMany(mappedBy = "shopOptionEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ShopOptionValueEntity> shopOptionValues;
}
