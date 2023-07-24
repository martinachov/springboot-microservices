package com.martinachov.inventoryservice.controller;

import com.martinachov.inventoryservice.model.Inventory;
import com.martinachov.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = InventoryController.class)
public class InventoryControllerTest {

    @MockBean
    private InventoryService inventoryService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void should_return_list_inventory_from_sku_codes() throws Exception {

        List<String> skuCodes = new ArrayList<String>(Arrays.asList("uno","dos"));
        Inventory inv1 = Inventory.builder().skuCode("uno").quantity(10).build();
        Inventory inv2 = Inventory.builder().skuCode("dos").quantity(0).build();
        List<Inventory> listOfInventory = new ArrayList<Inventory>(Arrays.asList(inv1,inv2));
        when(inventoryService.isInStock(skuCodes)).thenReturn(listOfInventory);

        mockMvc.perform(get("/api/inventory")
                .param("skuCodes", new String[]{"uno","dos"}))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andDo(print());
    }
}