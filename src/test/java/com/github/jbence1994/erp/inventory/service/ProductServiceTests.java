package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductServiceTests {
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final ProductService productService = new ProductService(productRepository);

    @Test
    public void getProductTest_HappyPath() {
        when(productRepository.findById(any())).thenReturn(Optional.of(product1()));

        var result = assertDoesNotThrow(() -> productService.getProduct(1L));

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getProductTest_UnhappyPath() {
        when(productRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(
                ProductNotFoundException.class,
                () -> productService.getProduct(3L)
        );
    }

    @Test
    public void updateProductTest_HappyPath() {
        when(productRepository.save(any())).thenReturn(product1());

        productService.updateProduct(product1());

        verify(productRepository, times(1)).save(product1());
    }
}
