package com.my.interrior.client.ordered;

import java.util.List;

import com.my.interrior.client.shop.ShopEntity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderedDTO {

	private List<OrderedEntity> orderedEntities;
	private List<ShopEntity> shopEntities;
}
