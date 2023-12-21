package com.food.ordering.system.order.service.messaging.listener.kafka;

import com.food.ordering.system.kafka.consumer.KafkaConsumer;
import com.food.ordering.system.kafka.order.avro.model.OrderApprovalStatus;
import com.food.ordering.system.kafka.order.avro.model.RestaurantApprovalResponseAvroModel;
import com.food.ordering.system.order.service.domain.dto.message.RestaurantApprovalResponse;
import com.food.ordering.system.order.service.domain.entity.Order;
import com.food.ordering.system.order.service.domain.ports.input.message.listener.restaurantapproval.RestaurantApproveResponseMessageListener;
import com.food.ordering.system.order.service.messaging.mapper.OrderMessagingDataMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestaurantApprovalResponseKafkaListener implements KafkaConsumer<RestaurantApprovalResponseAvroModel> {

	private final OrderMessagingDataMapper orderMessagingDataMapper;
	private final RestaurantApproveResponseMessageListener restaurantApproveResponseMessageListener;

	@Override
	@KafkaListener(id = "${kafka-consumer-config.restaurant-approval-consumer-group-id}",
			topics = "${order-service.restaurant-approval-response-topic-name}")
	public void receive(@Payload List<RestaurantApprovalResponseAvroModel> messages,
						@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
						@Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
						@Header(KafkaHeaders.OFFSET) List<Long> offsets) {
		log.info("{} number of restaurant approval responses received with keys:{}, partitions:{} and offsets:{}",
				messages.size(), keys, partitions, offsets);
		messages.forEach(message -> {
			RestaurantApprovalResponse response =
					orderMessagingDataMapper.restaurantApprovalResponseAvroModelToRestaurantApprovalResponse(message);
			if (OrderApprovalStatus.APPROVED == message.getOrderApprovalStatus()) {
				log.info("Processing approved order for order id: {}", message.getOrderId());
				restaurantApproveResponseMessageListener.orderApproved(response);
			} else if (OrderApprovalStatus.REJECTED == message.getOrderApprovalStatus()) {
				log.info("Processing rejected order for order id: {} with failure messages: {}",
						message.getOrderId(), String.join(Order.FAILURE_MESSAGE_DELIMITER, message.getFailureMessages()));
				restaurantApproveResponseMessageListener.orderRejected(response);
			}
		});
	}

}
