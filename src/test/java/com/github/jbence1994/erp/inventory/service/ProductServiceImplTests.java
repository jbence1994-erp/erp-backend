package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product1;
import static com.github.jbence1994.erp.inventory.testobject.ProductTestObject.product2;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    public void getProductsTest() {
        when(productRepository.findAll()).thenReturn(List.of(product1(), product2()));

        var result = productService.getProducts();

        assertFalse(result.isEmpty());
        assertEquals(2, result.size());
    }

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
                () -> productService.getProduct(1L)
        );
    }

    @Test
    public void createProductTest_HappyPath() {
        when(productRepository.save(any())).thenReturn(product1());

        var result = productService.createProduct(product1());

        assertEquals(product1().getId(), result.getId());
        assertEquals(product1().getName(), result.getName());
        assertEquals(product1().getSerialNumber(), result.getSerialNumber());
        assertEquals(product1().getPrice(), result.getPrice());
        assertEquals(product1().getUnit(), result.getUnit());
        assertEquals(product1().getDescription(), result.getDescription());
        assertEquals(product1().getSupplier().getId(), result.getSupplier().getId());
        assertEquals(product1().getSupplier().getName(), result.getSupplier().getName());
        assertEquals(product1().getSupplier().getPhone(), result.getSupplier().getPhone());
        assertEquals(product1().getSupplier().getEmail(), result.getSupplier().getEmail());
        assertEquals(product1().getOnStock(), result.getOnStock());
        assertEquals(product1().getPhotoFileName(), result.getPhotoFileName());
    }

    @Test
    public void updateProductTest_HappyPath() {
        when(productRepository.save(any())).thenReturn(product1());

        productService.updateProduct(product1());

        verify(productRepository, times(1)).save(any());
    }
}
