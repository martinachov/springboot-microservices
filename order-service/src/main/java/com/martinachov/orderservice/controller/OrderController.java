package com.martinachov.orderservice.controller;

import com.martinachov.orderservice.dto.OrderRequestDTO;
import com.martinachov.orderservice.dto.OrderResponseDTO;
import com.martinachov.orderservice.model.Order;
import com.martinachov.orderservice.model.OrderLineItems;
import com.martinachov.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;
    private final ModelMapper modelMapper;
    @PostMapping
    public ResponseEntity<OrderResponseDTO> placeOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        List<OrderLineItems> listItems = orderRequestDTO.getListOrderLineItemsDTO().stream()
                .map(itemDTO -> modelMapper.map(itemDTO, OrderLineItems.class))
                .collect(Collectors.toList());

        Order newOrder = orderService.placeOrder(listItems);
        return new ResponseEntity<>(OrderResponseDTO.builder().orderNumber(newOrder.getOrderNumber()).build(), HttpStatus.CREATED);
    }

    @RequestMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrder() {
        List<Order> listOrder = orderService.getAllOrder();
        List<OrderResponseDTO> resp = listOrder.stream().map(order -> modelMapper.map(order, OrderResponseDTO.class)).collect(Collectors.toList());
        return ResponseEntity.ok().body(resp);
    }
}
