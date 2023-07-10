package com.martinachov.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinachov.productservice.dto.ProductDTO;
import com.martinachov.productservice.exception.ProductNotFoundException;
import com.martinachov.productservice.model.Product;
import com.martinachov.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
class ProductControllerWebMvcTest {

    @MockBean
    private ProductService productService;

    @MockBean
    private ModelMapper modelMapper;

    /**
     * Simula la llamada a un endpoint
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void should_return_not_found_product_by_id() throws Exception {
        String id = "id_nonexistent";
        when(productService.findOneById(id)).thenT ion.class);
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_product_by_id() throws Exception {
        String id = "prod_1";
        Product product = Product.builder().id(id).name("Product 1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        when(productService.findOneById(id)).thenReturn(product);
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.description").value("Product 1"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(1000)))
                .andDo(print());
    }

    @Test
    public void should_create_product() throws Exception {
        String id = "prod_1";
        Product product = Product.builder().id(id).name("product_1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        mockMvc.perform(post("/api/products").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    public void should_return_list_of_two_products() throws Exception {
        Product product1 = Product.builder().id("id1").name("product_1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        Product product2 = Product.builder().id("id2").name("product_2").description("Product 2").price(BigDecimal.valueOf(1000)).build();
        List<Product> products = new ArrayList<>(Arrays.asList(product1,product2));
        when(productService.getAllProducts()).thenReturn(products);
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }

    @Test
    public void should_update_product() throws Exception {
        String id = "prod_1";
        Product updatedProduct = Product.builder().id(id).name("product_1").description("Product 1").price(BigDecimal.valueOf(2000)).build();
        mockMvc.perform(put("/api/products/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updatedProduct)))
                .andExpect(status().isOk())
                .andDo(print());

    }
}