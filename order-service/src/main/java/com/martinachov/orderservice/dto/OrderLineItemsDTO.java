package com.martinachov.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDTO {

    @NotBlank(message = "El codigo SKU no puede estar vacio")
    private String skuCode;
    @Positive(message = "El precio debe ser un numero positivo")
    private BigDecimal price;
    @Positive(message = "La cantidad indicada debe ser mayor a 0")
    private Integer quantity;

}
