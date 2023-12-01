package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.event.OrderCancelledEvent;
import com.food.ordering.system.order.service.domain.event.OrderCreatedEvent;
import com.food.ordering.system.order.service.domain.event.OrderPaidEvent;

import java.util.List;

// mj: is somehow similar to `use cases` of clean architecture
// mj: and application services are not use cases, because they do not have logic
public interface OrderDomainService {

	// mj: events are created in domain service, but are fired in application service
	// mj: naturally domain entities are responsible for creating events, because domain service is not mandatory
	// mj: domain service is required if we have access to multiple aggregates in business logic
	// 		or having some logic that doesn't fit into any single entity
	// mj: but, it's ok to have domain services for all entities as well
	OrderCreatedEvent validateAndInitiateOrder(Order order, Restaurant restaurant);

	OrderPaidEvent payOrder(Order order);

	// mj: no return event because it is an end state
	void approveOrder(Order order);

	OrderCancelledEvent cancelOrderPayment(Order order, List<String> failureMessages);

	// no return event because it is an end state
	void cancelOrder(Order order, List<String> failureMessages);

}
