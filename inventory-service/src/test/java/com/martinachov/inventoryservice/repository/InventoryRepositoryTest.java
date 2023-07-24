package com.martinachov.inventoryservice.repository;

import com.martinachov.inventoryservice.model.Inventory;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class InventoryRepositoryTest {

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void setUp() {
        inventoryRepository.deleteAll();
    }
    @Test
    public void should_find_no_inventory_if_repo_is_empty(){
        List<Inventory> list = inventoryRepository.findAll();
        assertTrue(list.isEmpty());
    }

    @Test
    public void should_return_inventory_of_product_with_sku() {
        Inventory inventory = Inventory.builder().skuCode("inv1").quantity(10).build();
        entityManager.persist(inventory);
        Optional<Inventory> inventoryFound = inventoryRepository.findBySkuCode(inventory.getSkuCode());
        assertEquals(inventory, inventoryFound.get());
    }

    @Test
    public void should_return_list_inventory_from_sku_code_list(){
        Inventory inv1 = Inventory.builder().skuCode("uno").quantity(10).build();
        Inventory inv2 = Inventory.builder().skuCode("dos").quantity(20).build();
        Inventory inv3 = Inventory.builder().skuCode("tres").quantity(30).build();
        Inventory inv4 = Inventory.builder().skuCode("cuatro").quantity(40).build();

        entityManager.persist(inv1);
        entityManager.persist(inv2);
        entityManager.persist(inv3);
        entityManager.persist(inv4);

        List<String> skuCodes = new ArrayList<String>(Arrays.asList(inv1.getSkuCode(),inv2.getSkuCode()));
        List<Inventory> inventoryList = inventoryRepository.findBySkuCodeIn(skuCodes);

        assertEquals(2, inventoryList.size());
    }

}