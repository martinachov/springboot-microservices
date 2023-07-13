package com.martinachov.orderservice.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequestDTO {
    @NotEmpty(message = "La lista de items de la orden no puede estar vacia")
    List<OrderLineItemsDTO> listOrderLineItemsDTO;
}
