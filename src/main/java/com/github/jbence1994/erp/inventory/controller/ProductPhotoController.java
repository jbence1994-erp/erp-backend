package com.github.jbence1994.erp.inventory.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.inventory.dto.CreateProductPhotoDto;
import com.github.jbence1994.erp.inventory.dto.ProductPhotoDto;
import com.github.jbence1994.erp.inventory.exception.ProductAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.inventory.exception.ProductDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.inventory.exception.ProductNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDeleteException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoDownloadException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoNotFoundException;
import com.github.jbence1994.erp.inventory.exception.ProductPhotoUploadException;
import com.github.jbence1994.erp.inventory.model.Product;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@AllArgsConstructor
public class ProductPhotoController {
    private final PhotoService<CreateProductPhotoDto, ProductPhotoDto, Product> photoService;
    private final MultipartFileToCreatePhotoDtoMapper<CreateProductPhotoDto> toCreatePhotoDtoMapper;

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
                ProductAlreadyHasAPhotoUploadedException exception
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
        } catch (
                ProductNotFoundException |
                ProductPhotoNotFoundException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        } catch (ProductPhotoDownloadException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProductPhoto(@PathVariable Long productId) {
        try {
            photoService.deletePhoto(productId);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (ProductDoesNotHaveAPhotoUploadedYetException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        } catch (ProductPhotoDeleteException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }
}
