package com.food.ordering.system.order.service.domain.entity;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.OrderId;
import com.food.ordering.system.order.service.domain.valueobject.OrderItemId;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class OrderItem extends BaseEntity<OrderItemId> {

	private OrderId orderId;
	private final Product product;
	private final int quantity;
	private final Money price;
	/**
	 * quantity * price
	 */
	private final Money subTotal;

	void initializeOrderItem(OrderId orderId, OrderItemId orderItemId) {
		this.orderId = orderId;
		super.setId(orderItemId);
	}

	boolean isPriceValid() {
		return price.isGreaterThanZero()
				&& price.equals(product.getPrice())
				&& price.multiply(quantity).equals(subTotal);
	}

}
