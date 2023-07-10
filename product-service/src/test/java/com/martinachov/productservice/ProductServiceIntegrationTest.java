package com.martinachov.productservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinachov.productservice.model.Product;
import com.martinachov.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductServiceIntegrationTest {
    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:4.4.2");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private ProductService productService;

    static {
        mongoDBContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
        dymDynamicPropertyRegistry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    public void setup(){
        productService.deleteAllProduct();
    }

    @Test
    public void should_return_not_found_product_by_id() throws Exception {
        String id = "id_nonexistent";
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void should_return_product_by_id() throws Exception {
        String id = "prod_1";
        Product product = Product.builder().id(id).name("Product 1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        productService.createProduct(product);
        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Product 1"))
                .andExpect(jsonPath("$.description").value("Product 1"))
                .andExpect(jsonPath("$.price").value(BigDecimal.valueOf(1000)))
                .andDo(print());
    }

    @Test
    public void should_create_a_product() throws Exception {
        String id = "prod_1";
        Product product = Product.builder().id(id).name("Product 1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product))).andExpect(status().isCreated());

        assertEquals(1, productService.getAllProducts().size());
    }

    @Test
    public void should_return_all_product_created() throws Exception {

        String id1 = "prod_1";
        Product product1 = Product.builder().id(id1).name("Product 1").description("Product 1").price(BigDecimal.valueOf(1000)).build();
        String id2 = "prod_2";
        Product product2 = Product.builder().id(id2).name("Product 2").description("Product 2").price(BigDecimal.valueOf(1000)).build();

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0))
                .andDo(print());

        productService.createProduct(product1);
        productService.createProduct(product2);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());

    }
}