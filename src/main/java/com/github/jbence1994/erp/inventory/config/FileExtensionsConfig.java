package com.github.jbence1994.erp.inventory.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "file-extensions-config")
@AllArgsConstructor
@Getter
public class FileExtensionsConfig {
    private List<String> allowedFileExtensions;
}
