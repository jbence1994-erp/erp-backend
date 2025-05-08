package com.github.jbence1994.erp.common.validation;

import com.github.jbence1994.erp.common.config.FileExtensionsConfig;
import com.github.jbence1994.erp.common.dto.CreatePhotoDto;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.ALLOWED_FILE_EXTENSIONS;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.BMP;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_NAME_BMP;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_NAME_JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_NAME_JPG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.FILE_NAME_PNG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPEG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.JPG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.PNG;
import static com.github.jbence1994.erp.common.constant.PhotoTestConstants.TXT;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FileValidatorImplTests {

    @Mock
    private FileExtensionsConfig fileExtensionsConfig;

    @InjectMocks
    private FileValidatorImpl fileValidator;

    @Mock
    private CreatePhotoDto createPhotoDto;

    @BeforeEach
    public void setup() {
        when(createPhotoDto.isEmpty()).thenReturn(false);

        lenient().when(fileExtensionsConfig.getAllowedFileExtensions()).thenReturn(ALLOWED_FILE_EXTENSIONS);
    }

    private static Stream<Arguments> fileNameAndExtensionParams() {
        return Stream.of(
                Arguments.of(JPG, FILE_NAME_JPG, JPG),
                Arguments.of(JPEG, FILE_NAME_JPEG, JPEG),
                Arguments.of(PNG, FILE_NAME_PNG, PNG),
                Arguments.of(BMP, FILE_NAME_BMP, BMP)
        );
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("fileNameAndExtensionParams")
    public void validateTest_HappyPath(
            String testCase,
            String fileName,
            String extension
    ) {
        when(createPhotoDto.getFileExtension()).thenReturn(extension);

        assertDoesNotThrow(() -> fileValidator.validate(createPhotoDto));
    }

    @ParameterizedTest(name = "{index} => {0}")
    @MethodSource("fileNameAndExtensionParams")
    public void validateTest_UnhappyPath_FileIsEmpty(
            String testCase,
            String fileName
    ) {
        when(createPhotoDto.isEmpty()).thenReturn(true);
        when(createPhotoDto.getOriginalFilename()).thenReturn(fileName);

        assertThrows(
                EmptyFileException.class,
                () -> fileValidator.validate(createPhotoDto)
        );
    }

    @Test
    public void validateTest_UnhappyPath_FileHasNotAllowedExtension() {
        when(createPhotoDto.getFileExtension()).thenReturn(TXT);

        assertThrows(
                InvalidFileExtensionException.class,
                () -> fileValidator.validate(createPhotoDto)
        );
    }
}
