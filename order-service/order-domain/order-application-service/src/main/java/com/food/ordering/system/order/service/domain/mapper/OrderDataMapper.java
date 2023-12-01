package com.food.ordering.system.order.service.domain.mapper;

import com.food.ordering.system.domain.valueobject.CustomerId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.valueobject.StreetAddress;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
// mj: it is actually a factory class
public class OrderDataMapper {

	public Restaurant orderCommandToRestaurant(CreateOrderCommand createOrderCommand) {
		return Restaurant.builder()
				.id(new RestaurantId(createOrderCommand.restaurantId()))
				.products(createOrderCommand.items().stream()
						.map(OrderItem::productId)
						.map(id -> Product.builder().id(new ProductId(id)).build())
						.collect(Collectors.toList()))
				.build();
	}

	public Order orderCommandToOrder(CreateOrderCommand createOrderCommand) {
		return Order.builder()
				.customerId(new CustomerId(createOrderCommand.customerId()))
				.restaurantId(new RestaurantId(createOrderCommand.restaurantId()))
				.deliveryAddress(orderAddressToStreetAddress(createOrderCommand.address()))
				.price(new Money(createOrderCommand.price()))
				.items(orderItemsToOrderItemEntities(createOrderCommand.items()))
				.build();
	}

	public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
		return CreateOrderResponse.builder()
				.orderTrackingId(order.getTrackingId().getValue())
				.orderStatus(order.getOrderStatus())
				.message(message)
				.build();
	}

	public TrackOrderResponse orderToTrackOrderResponse(Order order) {
		return TrackOrderResponse.builder()
				.orderTrackingId(order.getTrackingId().getValue())
				.orderStatus(order.getOrderStatus())
				.failureMessages(order.getFailureMessages())
				.build();
	}

	private List<com.food.ordering.system.order.service.domain.entity.OrderItem> orderItemsToOrderItemEntities(List<OrderItem> items) {
		return items.stream().map(orderItem -> com.food.ordering.system.order.service.domain.entity.OrderItem.builder()
						.product(Product.builder().id(new ProductId(orderItem.productId())).build())
						.price(new Money(orderItem.price()))
						.quantity(orderItem.quantity())
						.subTotal(new Money(orderItem.subTotal()))
						.build())
				.collect(Collectors.toList());
	}

	private StreetAddress orderAddressToStreetAddress(OrderAddress address) {
		return new StreetAddress(UUID.randomUUID(), address.street(), address.postalCode(), address.city());
	}

}
