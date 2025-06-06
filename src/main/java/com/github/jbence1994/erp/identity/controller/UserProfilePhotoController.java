package com.github.jbence1994.erp.identity.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.identity.dto.CreateUserProfilePhotoDto;
import com.github.jbence1994.erp.identity.dto.UserProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.UserProfileAlreadyHasAPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.UserProfileDoesNotHaveAPhotoUploadedYetException;
import com.github.jbence1994.erp.identity.exception.UserProfileNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDeleteException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoDownloadException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.UserProfilePhotoUploadException;
import com.github.jbence1994.erp.identity.model.UserProfile;
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
@RequestMapping("/api/userProfiles/{userProfileId}/photo")
@CrossOrigin
@AllArgsConstructor
public class UserProfilePhotoController {
    private final PhotoService<CreateUserProfilePhotoDto, UserProfilePhotoDto, UserProfile> photoService;
    private final MultipartFileToCreatePhotoDtoMapper<CreateUserProfilePhotoDto> toCreatePhotoDtoMapper;

    @PostMapping
    public ResponseEntity<?> uploadUserProfilePhoto(
            @PathVariable Long userProfileId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            var photoDto = toCreatePhotoDtoMapper.toDto(userProfileId, file);

            var photoFileName = photoService.uploadPhoto(photoDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new PhotoResponse(photoFileName));
        } catch (UserProfileNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        } catch (
                EmptyFileException |
                InvalidFileExtensionException |
                UserProfileAlreadyHasAPhotoUploadedException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        } catch (UserProfilePhotoUploadException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserProfilePhoto(@PathVariable Long userProfileId) {
        try {
            var photo = photoService.getPhoto(userProfileId);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(String.format("image/%s", photo.getFileExtension())));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(photo.getPhotoBytes());
        } catch (
                UserProfileNotFoundException |
                UserProfilePhotoNotFoundException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        } catch (UserProfilePhotoDownloadException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUserProfilePhoto(@PathVariable Long userProfileId) {
        try {
            photoService.deletePhoto(userProfileId);

            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (UserProfileDoesNotHaveAPhotoUploadedYetException exception) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        } catch (UserProfilePhotoDeleteException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }
}
