package com.food.ordering.system.order.service.dataaccess.restaurant.adapter;

import com.food.ordering.system.order.service.dataaccess.restaurant.mapper.RestaurantDataAccessMapper;
import com.food.ordering.system.order.service.dataaccess.restaurant.repository.RestaurantJpaRepository;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.ports.output.respository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

	private final RestaurantJpaRepository restaurantJpaRepository;
	private final RestaurantDataAccessMapper restaurantDataAccessMapper;

	@Override
	public Optional<Restaurant> findRestaurantInformation(Restaurant restaurant) {
		List<UUID> productIds = restaurantDataAccessMapper.restaurantToRestaurantProducts(restaurant);
		return restaurantJpaRepository.findByRestaurantIdAndProductIdIn(restaurant.getId().getValue(), productIds)
				.map(restaurantDataAccessMapper::restaurantEntityToRestaurant);
	}

}
