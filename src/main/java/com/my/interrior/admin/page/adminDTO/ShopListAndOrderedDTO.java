package com.my.interrior.admin.page.adminDTO;

import com.my.interrior.client.shop.ShopEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopListAndOrderedDTO {

	private ShopEntity shop;
	private int orderedCount;
	
	public ShopListAndOrderedDTO(ShopEntity shop, int orderedCount) {
		this.shop = shop;
		this.orderedCount = orderedCount;
	}
}
