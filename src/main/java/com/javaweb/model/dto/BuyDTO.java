package com.javaweb.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BuyDTO {
	@NotNull(message = "Id null")
	Long idVariant;
	@NotNull(message= "Id null")
	Integer quantity;
}
