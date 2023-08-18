package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Product extends BaseEntity<ProductId> {

	private String name;
	private Money price;

	public void updateWithDbProduct(Product dbProduct) {
		name = dbProduct.getName();
		price = dbProduct.getPrice();
	}

}
