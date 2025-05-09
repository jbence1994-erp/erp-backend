package com.github.jbence1994.erp.identity.controller;

import com.github.jbence1994.erp.common.dto.PhotoResponse;
import com.github.jbence1994.erp.common.exception.EmptyFileException;
import com.github.jbence1994.erp.common.exception.InvalidFileExtensionException;
import com.github.jbence1994.erp.common.mapper.MultipartFileToCreatePhotoDtoMapper;
import com.github.jbence1994.erp.common.service.PhotoService;
import com.github.jbence1994.erp.identity.dto.CreateProfilePhotoDto;
import com.github.jbence1994.erp.identity.exception.ProfileAlreadyHasPhotoUploadedException;
import com.github.jbence1994.erp.identity.exception.ProfileNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoNotFoundException;
import com.github.jbence1994.erp.identity.exception.ProfilePhotoUploadException;
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
@RequestMapping("/api/profiles/{profileId}/photo")
@CrossOrigin
public class ProfilePhotoController {
    private final PhotoService photoService;
    private final MultipartFileToCreatePhotoDtoMapper<CreateProfilePhotoDto> toCreatePhotoDtoMapper;

    public ProfilePhotoController(
            @Qualifier("profilePhotoService") PhotoService photoService,
            MultipartFileToCreatePhotoDtoMapper<CreateProfilePhotoDto> toCreatePhotoDtoMapper
    ) {
        this.photoService = photoService;
        this.toCreatePhotoDtoMapper = toCreatePhotoDtoMapper;
    }

    @GetMapping
    public ResponseEntity<?> getProfilePhoto(@PathVariable Long profileId) {
        try {
            var photo = photoService.getPhoto(profileId);

            var headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(String.format("image/%s", photo.getFileExtension())));

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .headers(headers)
                    .body(photo.getPhotoBytes());
        } catch (ProfilePhotoNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> uploadProfilePhoto(
            @PathVariable Long profileId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            var profilePhotoDto = toCreatePhotoDtoMapper.toDto(profileId, file);

            var profilePhotoFileName = photoService.uploadPhoto(profilePhotoDto);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new PhotoResponse(profilePhotoFileName));
        } catch (ProfileNotFoundException exception) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(exception.getMessage());
        } catch (
                EmptyFileException |
                InvalidFileExtensionException |
                ProfileAlreadyHasPhotoUploadedException exception
        ) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(exception.getMessage());
        } catch (ProfilePhotoUploadException exception) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(exception.getMessage());
        }
    }
}
