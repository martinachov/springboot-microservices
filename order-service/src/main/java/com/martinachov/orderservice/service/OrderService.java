package com.martinachov.orderservice.service;

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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public Order placeOrder(List<OrderLineItems> listOrderItems) {
        Order newOrder = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(listOrderItems)
                .build();

        orderRepository.save(newOrder);
        return newOrder;
    }

    public List<Order> getAllOrder() {
        return orderRepository.findAll();
    }

}
