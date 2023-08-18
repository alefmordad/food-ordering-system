package com.food.ordering.system.domain.entity;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class BaseEntity<ID> {
	private ID id;
}
