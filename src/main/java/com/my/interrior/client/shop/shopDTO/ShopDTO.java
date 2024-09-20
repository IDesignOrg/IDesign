package com.my.interrior.client.shop.shopDTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopDTO {
	private String title;
    private String category;
    private String discountRate;
    private String price;
    private List<ShopOptionDTO> option;
}
