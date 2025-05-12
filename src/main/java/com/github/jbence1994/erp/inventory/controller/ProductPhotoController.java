package com.github.jbence1994.erp.inventory.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products/{productId}/photo")
@CrossOrigin
public class ProductPhotoController {
    @Qualifier("productPhotoService")
    private final PhotoService photoService;
    private final MultipartFileToCreatePhotoDtoMapper<CreateProductPhotoDto> toCreatePhotoDtoMapper;

    public ProductPhotoController(
            @Qualifier("productPhotoService") PhotoService photoService,
            MultipartFileToCreatePhotoDtoMapper<CreateProductPhotoDto> toCreatePhotoDtoMapper
    ) {
        this.photoService = photoService;
        this.toCreatePhotoDtoMapper = toCreatePhotoDtoMapper;
    }

    @GetMapping
    public ResponseEntity<?> getProductPhoto(@PathVariable Long productId) {
        try {
            var photo = photoService.getPhoto(productId);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(String.format("image/%s", photo.getFileExtension())));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(photo.getPhotoBytes());
        } catch (ProductPhotoNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadProductPhoto(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            var photoDto = toCreatePhotoDtoMapper.toDto(productId, file);

            var photoFileName = photoService.uploadPhoto(photoDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new PhotoResponse(photoFileName));
        } catch (ProductNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        } catch (
                EmptyFileException |
                InvalidFileExtensionException |
                ProductAlreadyHasPhotoUploadedException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        } catch (ProductPhotoUploadException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }
}
