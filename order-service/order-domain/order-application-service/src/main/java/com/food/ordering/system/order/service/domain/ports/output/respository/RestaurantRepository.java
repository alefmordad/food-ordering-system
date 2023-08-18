package com.food.ordering.system.order.service.domain.ports.output.respository;

import com.food.ordering.system.order.service.domain.entity.Restaurant;

import java.util.Optional;

public interface RestaurantRepository {

	// filling details of products
	Optional<Restaurant> findRestaurantInformation(Restaurant restaurant);

}
