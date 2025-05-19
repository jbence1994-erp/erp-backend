package com.github.jbence1994.erp.inventory.service;

import com.github.jbence1994.erp.inventory.repository.SupplierRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.github.jbence1994.erp.inventory.testobject.SupplierTestObject.supplier1;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SupplierServiceImplTests {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierServiceImpl supplierService;

    @Test
    public void createSupplierTest_HappyPath() {
        when(supplierRepository.save(any())).thenReturn(supplier1());

        var result = supplierService.createSupplier(supplier1());

        assertEquals(supplier1().getId(), result.getId());
        assertEquals(supplier1().getName(), result.getName());
        assertEquals(supplier1().getPhone(), result.getPhone());
        assertEquals(supplier1().getEmail(), result.getEmail());
    }

    @Test
    public void updateSupplierTest_HappyPath() {
        when(supplierRepository.save(any())).thenReturn(supplier1());

        supplierService.updateSupplier(supplier1());

        verify(supplierRepository, times(1)).save(any());
    }
}
