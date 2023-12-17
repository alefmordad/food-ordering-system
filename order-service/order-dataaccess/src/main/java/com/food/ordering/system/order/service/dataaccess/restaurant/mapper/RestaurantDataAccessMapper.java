package com.food.ordering.system.order.service.dataaccess.restaurant.mapper;

import com.food.ordering.system.domain.entity.BaseEntity;
import com.food.ordering.system.domain.valueobject.BaseId;
import com.food.ordering.system.domain.valueobject.Money;
import com.food.ordering.system.domain.valueobject.ProductId;
import com.food.ordering.system.domain.valueobject.RestaurantId;
import com.food.ordering.system.order.service.dataaccess.restaurant.entity.RestaurantEntity;
import com.food.ordering.system.order.service.dataaccess.restaurant.exception.RestaurantDataAccessException;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class RestaurantDataAccessMapper {

	public List<UUID> restaurantToRestaurantProducts(Restaurant restaurant) {
		return restaurant.getProducts().stream()
				.map(BaseEntity::getId)
				.map(BaseId::getValue)
				.collect(Collectors.toList());
	}

	public Restaurant restaurantEntityToRestaurant(List<RestaurantEntity> restaurantEntities) {
		RestaurantEntity restaurantEntity = restaurantEntities.stream().findFirst().orElseThrow(() ->
				new RestaurantDataAccessException("Restaurant could not be found!"));
		List<Product> products = restaurantEntities.stream().map(restaurant -> Product.builder()
						.id(new ProductId(restaurant.getProductId()))
						.name(restaurant.getRestaurantName())
						.price(new Money(restaurant.getProductPrice()))
						.build())
				.collect(Collectors.toList());
		return Restaurant.builder()
				.id(new RestaurantId(restaurantEntity.getRestaurantId()))
				.active(restaurantEntity.getRestaurantActive())
				.products(products)
				.build();
	}

}
