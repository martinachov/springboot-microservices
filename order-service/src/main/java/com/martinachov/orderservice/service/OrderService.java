package com.martinachov.orderservice.service;

import com.martinachov.orderservice.dto.InventoryResponseDTO;
import com.martinachov.orderservice.dto.OrderDTO;
import com.martinachov.orderservice.dto.OrderRequestDTO;
import com.martinachov.orderservice.dto.OrderResponseDTO;
import com.martinachov.orderservice.model.Order;
import com.martinachov.orderservice.model.OrderLineItems;
import com.martinachov.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;

    public Order placeOrder(List<OrderLineItems> listOrderItems) {
        Order newOrder = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(listOrderItems)
                .build();

        List<String> skuCodes = listOrderItems.stream()
                .map(OrderLineItems::getSkuCode)
                .collect(Collectors.toList());

        // Call Inventory Service
        InventoryResponseDTO[] response = webClientBuilder.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCodes", skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(InventoryResponseDTO[].class)
                .block();

        // Save new Order if all products are in stock
        boolean allInStock = Arrays.stream(response).allMatch(resp -> resp.isInStock());
        if(allInStock){
            orderRepository.save(newOrder);
        }else {
            throw new IllegalArgumentException("Product is not in stock, please try again later");
        }
        return newOrder;
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

}
