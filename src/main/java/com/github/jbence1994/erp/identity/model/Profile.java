package com.github.jbence1994.erp.identity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.StringUtils;

@Entity
@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String photoFileName;

    private boolean isDeleted;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public boolean hasPhoto() {
        return photoFileName != null;
    }

    public String getPhotoFileExtension() {
        return StringUtils.getFilenameExtension(photoFileName);
    }
}
