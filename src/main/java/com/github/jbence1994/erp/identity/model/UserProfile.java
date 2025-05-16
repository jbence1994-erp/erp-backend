package com.github.jbence1994.erp.identity.model;

import com.github.jbence1994.erp.common.model.PhotoEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "user_profiles")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile implements PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String photoFileName;

    private boolean isDeleted;

    @Override
    public boolean hasPhoto() {
        return photoFileName != null;
    }

    @Override
    public String getPhotoFileName() {
        return photoFileName;
    }

    @Override
    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    @Override
    public String getPhotoFileExtension() {
        return StringUtils.getFilenameExtension(photoFileName);
    }
}
