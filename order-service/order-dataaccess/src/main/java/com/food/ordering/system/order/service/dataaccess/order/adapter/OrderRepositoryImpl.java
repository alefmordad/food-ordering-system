package com.food.ordering.system.order.service.dataaccess.order.adapter;

import com.food.ordering.system.order.service.dataaccess.order.entity.OrderEntity;
import com.food.ordering.system.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.output.respository.OrderRepository;
import com.food.ordering.system.order.service.domain.valueobject.TrackingId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;
	private final OrderDataAccessMapper orderDataAccessMapper;

	@Override
	public Order save(Order order) {
		OrderEntity entity = orderDataAccessMapper.orderToOrderEntity(order);
		entity = orderJpaRepository.save(entity);
		return orderDataAccessMapper.orderEntityToOrder(entity);
	}

	@Override
	public Optional<Order> findByTrackingId(TrackingId trackingId) {
		return orderJpaRepository.findByTrackingId(trackingId.getValue())
				.map(orderDataAccessMapper::orderEntityToOrder);
	}

}
