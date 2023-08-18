package com.food.ordering.system.order.service.domain.valueobject;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.UUID;

@Value
@EqualsAndHashCode(exclude = "id")
public class StreetAddress {
	UUID id;
	String street;
	String postalCode;
	String city;
}
