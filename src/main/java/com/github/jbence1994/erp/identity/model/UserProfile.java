package com.github.jbence1994.erp.identity.model;

import com.github.jbence1994.erp.common.model.PhotoEntity;
import com.github.jbence1994.erp.common.model.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserProfile extends PhotoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String photoFileName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private boolean isDeleted;

    @Override
    public String getPhotoFileName() {
        return photoFileName;
    }

    @Override
    public void setPhotoFileName(String photoFileName) {
        this.photoFileName = photoFileName;
    }

    public void updatePassword(String newPassword) {
        password = newPassword;
    }

    public void delete() {
        isDeleted = true;
    }

    public void restore() {
        isDeleted = false;
    }
}
