package com.martinachov.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.martinachov.orderservice.dto.OrderDTO;
import com.martinachov.orderservice.dto.OrderLineItemsDTO;
import com.martinachov.orderservice.dto.OrderRequestDTO;
import com.martinachov.orderservice.model.Order;
import com.martinachov.orderservice.model.OrderLineItems;
import com.martinachov.orderservice.service.OrderService;
import jakarta.validation.constraints.NotEmpty;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
public class OrderControllerWebMvcTest {

    @MockBean
    private OrderService orderService;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @Test
    public void should_return_all_orders() throws  Exception {
        Order order1 = Order.builder().build();
        Order order2 = Order.builder().build();
        List<Order> list = new ArrayList<>(Arrays.asList(order1,order2));

        when(orderService.getAllOrder()).thenReturn(list);

        mockMvc.perform(get("/api/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(list.size()))
                .andDo(print());
    }

    @Test
    public void should_create_a_order() throws Exception {
        Order newOrder = createMockOrder();

        when(orderService.placeOrder(any())).thenReturn(newOrder);

        String jsonBody = "{\"listOrderLineItemsDTO\":[]}";
        mockMvc.perform(post("/api/order")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value(newOrder.getOrderNumber()))
                .andDo(print());
    }

    private Order createMockOrder(){

        OrderLineItems orderItem = OrderLineItems.builder()
                .id(1L)
                .price(BigDecimal.valueOf(100))
                .skuCode("1")
                .quantity(1)
                .build();

        List<OrderLineItems> listOrderItems = new ArrayList<>(Arrays.asList(orderItem));

        Order mockOrder = Order.builder()
                .orderNumber("1")
                .orderLineItemsList(listOrderItems)
                .build();

        return mockOrder;
    }
}
