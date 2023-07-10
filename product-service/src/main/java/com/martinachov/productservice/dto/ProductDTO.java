package com.martinachov.productservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private String id;

    @NotBlank(message = "El nombre del producto no puede estar vacio")
    @Size(min = 3, max = 20, message = "El nombre del producto debe contener entre 3 y 20 caracteres")
    private String name;

    @NotBlank(message = "La descripcion del producto no puede estar vacia")
    @Size(max = 50, message = "La descripcion del producto no puede exeder los 50 caracteres")
    private String description;

    @Positive(message = "El precio debe ser un numero positivo")
    private BigDecimal price;
}
