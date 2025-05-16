package com.github.jbence1994.erp.inventory.mapper;

import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static com.github.jbence1994.erp.common.testobject.MultipartFileTestObject.multipartFile;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class MultipartFileToCreateProductPhotoDtoMapperTests {

    @InjectMocks
    private MultipartFileToCreateProductPhotoDtoMapper toProductPhotoDtoMapper;

    @Test
    public void toDtoTest_HappyPath() throws IOException {
        var result = toProductPhotoDtoMapper.toDto(1L, multipartFile());

        assertEquals(1L, result.getEntityId());
        assertEquals(multipartFile().isEmpty(), result.isEmpty());
        assertEquals(multipartFile().getOriginalFilename(), result.getOriginalFilename());
        assertEquals(multipartFile().getSize(), result.getSize());
        assertEquals(multipartFile().getContentType(), result.getContentType());
        assertThat(multipartFile().getBytes()).containsExactly(result.getInputStreamBytes());
    }

    @Test
    public void toDtoTest_UnhappyPath() throws IOException {
        var multipartFile = spy(multipartFile());

        doThrow(new IOException("Disk error")).when(multipartFile).getBytes();

        assertThrows(
                ProductPhotoUploadException.class,
                () -> toProductPhotoDtoMapper.toDto(1L, multipartFile)
        );
    }
}
