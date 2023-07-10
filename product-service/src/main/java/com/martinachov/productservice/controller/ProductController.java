package com.martinachov.productservice.controller;

import com.martinachov.productservice.dto.ProductDTO;
import com.martinachov.productservice.exception.ProductNotFoundException;
import com.martinachov.productservice.model.Product;
import com.martinachov.productservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        List<Product> allProducts = productService.getAllProducts();
        List<ProductDTO> response = allProducts.stream()
                .map(product -> modelMapper.map(product, ProductDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductById(@PathVariable String id) {
        try {
            Product product = productService.findOneById(id);
            return ResponseEntity.ok(product);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@Valid @RequestBody  ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product created!");
    }

    @DeleteMapping(value= "/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable String id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.ok("Product deleted!");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<?> updateProductById(@PathVariable String id, @Valid @RequestBody ProductDTO detail) {
        try {
            productService.updateProductById(id, modelMapper.map(detail, Product.class));
            return ResponseEntity.ok("Product modified!");
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
