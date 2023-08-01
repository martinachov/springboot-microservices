package com.martinachov.inventoryservice.controller;

import com.martinachov.inventoryservice.dto.InventoryResponseDTO;
import com.martinachov.inventoryservice.model.Inventory;
import com.martinachov.inventoryservice.service.InventoryService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Setter
@Getter
@RefreshScope
public class InventoryController {

    private final InventoryService inventoryService;

    @Value("${app.minProducts}")
    private Long minProducts;

    // http://localhost:8082/api/inventory/iphone-13,iphone13-red
    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red

    @GetMapping
    public ResponseEntity<List<InventoryResponseDTO>> isInStock(@RequestParam List<String> skuCodes) {
        List<Inventory> listInventory = inventoryService.isInStock(skuCodes);
        List<InventoryResponseDTO> resp = listInventory
                .stream()
                .map(inventory -> InventoryResponseDTO.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > minProducts)
                        .build()
                ).collect(Collectors.toList());
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/minProducts")
    public ResponseEntity<Long> getMinProducts() {
        return ResponseEntity.ok(minProducts);
    }
}
