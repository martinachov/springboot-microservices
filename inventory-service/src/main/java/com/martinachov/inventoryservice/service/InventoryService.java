package com.martinachov.inventoryservice.service;

import com.martinachov.inventoryservice.model.Inventory;
import com.martinachov.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<Inventory> isInStock(List<String> skuCodes) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        return inventories;
    }
}
