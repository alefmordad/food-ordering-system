package com.food.ordering.system.order.service.domain.ports.input.message.listener.payment;

import com.food.ordering.system.order.service.domain.dto.message.PaymentResponse;

// domain event listeners are special type of application services which are triggered by firing domain events (not ui)
public interface PaymentResponseMessageListener {

	void paymentCompleted(PaymentResponse paymentResponse);

	void paymentCancelled(PaymentResponse paymentResponse);

}
