package com.my.interrior.client.shop.shopDTO;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShopOptionDTO {
	private String optionName;
    private List<ShopOptionValueDTO> options;
}
