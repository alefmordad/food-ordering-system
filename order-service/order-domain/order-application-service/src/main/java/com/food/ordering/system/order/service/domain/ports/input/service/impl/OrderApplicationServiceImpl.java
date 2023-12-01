package com.food.ordering.system.order.service.domain.ports.input.service.impl;

import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
// mj: @Validated here enables @Valid which is on the parameters of methods in interface
// mj: no need to be public
class OrderApplicationServiceImpl implements OrderApplicationService {

	private final OrderCreateCommandHandler orderCreateCommandHandler;
	private final OrderTrackQueryHandler orderTrackQueryHandler;

	@Override
	public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
		return orderCreateCommandHandler.createOrder(createOrderCommand);
	}

	@Override
	public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
		return orderTrackQueryHandler.trackOrder(trackOrderQuery);
	}

}
