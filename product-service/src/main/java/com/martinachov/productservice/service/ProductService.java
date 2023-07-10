package com.martinachov.productservice.service;

import com.martinachov.productservice.exception.ProductNotFoundException;
import com.martinachov.productservice.model.Product;
import com.martinachov.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.ProviderNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void createProduct(Product product){
        productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        List<Product> list = productRepository.findAll();
        return list;
    }

    public Product findOneById(String id) throws ProductNotFoundException {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct.orElseThrow(()-> new ProductNotFoundException("Product not found!"));
    }
    public void deleteProductById(String id) throws ProductNotFoundException {
        Optional<Product> optProduct = productRepository.findById(id);
        Product productToDelete = optProduct.orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        productRepository.delete(productToDelete);
    }

    public Product updateProductById(String id, Product productDetail) throws ProductNotFoundException {
        Optional<Product> optProduct = productRepository.findById(id);
        Product productToUpdate = optProduct.orElseThrow(() -> new ProductNotFoundException("Product not found!"));
        productToUpdate.setName(productDetail.getName());
        productToUpdate.setDescription(productDetail.getDescription());
        productToUpdate.setPrice(productDetail.getPrice());
        return productRepository.save(productToUpdate);
    }
}
