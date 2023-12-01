package com.food.ordering.system.order.service.domain;

import com.food.ordering.system.domain.valueobject.*;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderCommand;
import com.food.ordering.system.order.service.domain.dto.create.CreateOrderResponse;
import com.food.ordering.system.order.service.domain.dto.create.OrderAddress;
import com.food.ordering.system.order.service.domain.dto.create.OrderItem;
import com.food.ordering.system.order.service.domain.entity.Customer;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.entity.Product;
import com.food.ordering.system.order.service.domain.entity.Restaurant;
import com.food.ordering.system.order.service.domain.exception.OrderDomainException;
import com.food.ordering.system.order.service.domain.mapper.OrderDataMapper;
import com.food.ordering.system.order.service.domain.ports.input.service.OrderApplicationService;
import com.food.ordering.system.order.service.domain.ports.output.respository.CustomerRepository;
import com.food.ordering.system.order.service.domain.ports.output.respository.OrderRepository;
import com.food.ordering.system.order.service.domain.ports.output.respository.RestaurantRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// mj: default is PER_METHOD
// mj: if we use PER_CLASS, then we can have @BeforeAll on non-static methods
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = OrderTestConfiguration.class)
class OrderApplicationServiceTest {

	@Autowired private OrderApplicationService orderApplicationService;
	@Autowired private OrderDataMapper orderDataMapper;
	@Autowired private OrderRepository orderRepository;
	@Autowired private CustomerRepository customerRepository;
	@Autowired private RestaurantRepository restaurantRepository;

	private CreateOrderCommand createOrderCommand;
	private CreateOrderCommand createOrderCommandWrongPrice;
	private CreateOrderCommand createOrderCommandWrongProductPrice;
	private final UUID CUSTOMER_ID = UUID.fromString("c15f50ae-3e07-11ee-be56-0242ac120002");
	private final UUID RESTAURANT_ID = UUID.fromString("c15f50ae-3e07-11ee-be56-0242ac120003");
	private final UUID PRODUCT_ID = UUID.fromString("c15f50ae-3e07-11ee-be56-0242ac120004");
	private final UUID ORDER_ID = UUID.fromString("c15f50ae-3e07-11ee-be56-0242ac120005");
	private final BigDecimal PRICE = new BigDecimal("200.00");

	@BeforeAll
	public void init() {
		createOrderCommand = CreateOrderCommand.builder()
				.customerId(CUSTOMER_ID)
				.restaurantId(RESTAURANT_ID)
				.address(OrderAddress.builder()
						.street("street_1")
						.postalCode("1234AB")
						.city("Paris")
						.build())
				.price(PRICE)
				.items(List.of(
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(1)
								.price(new BigDecimal("50.00"))
								.subTotal(new BigDecimal("50.00"))
								.build(),
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(3)
								.price(new BigDecimal("50.00"))
								.subTotal(new BigDecimal("150.00"))
								.build()))
				.build();
		createOrderCommandWrongPrice = CreateOrderCommand.builder()
				.customerId(CUSTOMER_ID)
				.restaurantId(RESTAURANT_ID)
				.address(OrderAddress.builder()
						.street("street_1")
						.postalCode("1234AB")
						.city("Paris")
						.build())
				.price(new BigDecimal("250.00"))
				.items(List.of(
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(1)
								.price(new BigDecimal("50.00"))
								.subTotal(new BigDecimal("50.00"))
								.build(),
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(3)
								.price(new BigDecimal("50.00"))
								.subTotal(new BigDecimal("150.00"))
								.build()))
				.build();
		createOrderCommandWrongProductPrice = CreateOrderCommand.builder()
				.customerId(CUSTOMER_ID)
				.restaurantId(RESTAURANT_ID)
				.address(OrderAddress.builder()
						.street("street_1")
						.postalCode("1234AB")
						.city("Paris")
						.build())
				.price(new BigDecimal("210.00"))
				.items(List.of(
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(1)
								.price(new BigDecimal("60.00"))
								.subTotal(new BigDecimal("60.00"))
								.build(),
						OrderItem.builder()
								.productId(PRODUCT_ID)
								.quantity(3)
								.price(new BigDecimal("50.00"))
								.subTotal(new BigDecimal("150.00"))
								.build()))
				.build();

		Customer customer = Customer.builder().id(new CustomerId(CUSTOMER_ID)).build();
		Restaurant restaurantResponse = Restaurant.builder()
				.id(new RestaurantId(createOrderCommand.restaurantId()))
				.products(List.of(
						Product.builder()
								.id(new ProductId(PRODUCT_ID))
								.name("product-1")
								.price(new Money(new BigDecimal("50.00")))
								.build(),
						Product.builder()
								.id(new ProductId(PRODUCT_ID))
								.name("product-2")
								.price(new Money(new BigDecimal("50.00")))
								.build()))
				.active(true)
				.build();

		Order order = orderDataMapper.orderCommandToOrder(createOrderCommand);
		order.setId(new OrderId(ORDER_ID));

		when(customerRepository.findCustomer(CUSTOMER_ID)).thenReturn(Optional.of(customer));
		when(restaurantRepository.findRestaurantInformation(orderDataMapper.orderCommandToRestaurant(createOrderCommand)))
				.thenReturn(Optional.of(restaurantResponse));
		when(orderRepository.save(any(Order.class))).thenReturn(order);
	}

	@Test
	void testCreateOrder() {
		CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
		assertEquals(OrderStatus.PENDING, createOrderResponse.orderStatus());
		assertEquals("Order created successfully", createOrderResponse.message());
		assertNotNull(createOrderResponse.orderTrackingId());
	}

	@Test
	void testCreateOrderWithWrongTotalPrice() {
		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommandWrongPrice));
		assertEquals("Total price: 250.00 is not equal to Order items total: 200.00!",
				orderDomainException.getMessage());
	}

	@Test
	void testCreateOrderWithWrongProductPrice() {
		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommandWrongProductPrice));
		assertEquals("Order items price: 60.00 is not valid for product " + PRODUCT_ID,
				orderDomainException.getMessage());
	}

	@Test
	void testCreateOrderWithPassiveRestaurant() {
		Restaurant restaurantResponse = Restaurant.builder()
				.id(new RestaurantId(createOrderCommand.restaurantId()))
				.products(List.of(
						Product.builder()
								.id(new ProductId(PRODUCT_ID))
								.name("product-1")
								.price(new Money(new BigDecimal("50.00")))
								.build(),
						Product.builder()
								.id(new ProductId(PRODUCT_ID))
								.name("product-2")
								.price(new Money(new BigDecimal("50.00")))
								.build()))
				.active(false)
				.build();

		when(restaurantRepository.findRestaurantInformation(orderDataMapper.orderCommandToRestaurant(createOrderCommand)))
				.thenReturn(Optional.of(restaurantResponse));

		OrderDomainException orderDomainException = assertThrows(OrderDomainException.class,
				() -> orderApplicationService.createOrder(createOrderCommand));
		assertEquals("Restaurant with id " + RESTAURANT_ID + " is currently not active!",
				orderDomainException.getMessage());
	}

}
