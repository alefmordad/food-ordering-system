package com.food.ordering.system.order.service.domain.ports.input.service;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

import javax.validation.Valid;

// valid annotations must not be used in the implementations, but here in the interface
public interface OrderApplicationService {

	CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand);

	TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

}
