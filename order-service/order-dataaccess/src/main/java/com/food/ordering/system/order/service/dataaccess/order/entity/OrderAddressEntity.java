package com.food.ordering.system.order.service.dataaccess.order.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_addresses")
public class OrderAddressEntity {

	@Id
	private UUID id;

	@JoinColumn(name = "order_id")
	@OneToOne(cascade = CascadeType.ALL)
	private OrderEntity order;

	private String street;
	private String postalCode;
	private String city;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OrderAddressEntity that = (OrderAddressEntity) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

}
