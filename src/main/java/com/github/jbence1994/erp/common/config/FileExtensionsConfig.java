package com.github.jbence1994.erp.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "erp.file-extensions-config")
@Data
public class FileExtensionsConfig {
    private List<String> allowedFileExtensions;
}
