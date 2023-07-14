package com.martinachov.inventoryservice.controller;

import com.martinachov.inventoryservice.dto.InventoryResponseDTO;
import com.martinachov.inventoryservice.model.Inventory;
import com.martinachov.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red
    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> isInStock(@RequestParam List<String> skuCodes) {
        List<Inventory> listInventory = inventoryService.isInStock(skuCodes);
        List<InventoryResponseDTO> resp = listInventory
                .stream()
                .map(inventory -> InventoryResponseDTO.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }
}
