package com.github.jbence1994.erp.identity.mapper;

import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
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
class MultipartFileToCreateUserProfilePhotoDtoMapperTests {

    @InjectMocks
    private MultipartFileToCreateUserProfilePhotoDtoMapper toProfilePhotoDtoMapper;

    @Test
    public void toDtoTest_HappyPath() throws IOException {
        var result = toProfilePhotoDtoMapper.toDto(1L, multipartFile());

        assertEquals(1L, result.getUserProfileId());
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
                UserProfilePhotoUploadException.class,
                () -> toProfilePhotoDtoMapper.toDto(1L, multipartFile)
        );
    }
}
