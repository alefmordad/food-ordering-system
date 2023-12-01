package com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.impl;

import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApproveResponseMessageListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Service
@Validated
@RequiredArgsConstructor
public class RestaurantApprovalResponseMessageListenerImpl implements RestaurantApproveResponseMessageListener {

	@Override
	public void orderApproved(RestaurantApprovalResponse restaurantApprovalResponse) {

	}

	@Override
	public void orderRejected(RestaurantApprovalResponse restaurantApprovalResponse) {

	}

}
