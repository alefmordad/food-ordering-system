package com.food.ordering.system.order.service.domain.dto.create;

import lombok.Builder;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Builder
public record OrderAddress(@NotNull @Max(50) String street, @NotNull @Max(10) String postalCode,
						   @NotNull @Max(50) String city) {
}
