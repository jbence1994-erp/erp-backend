package com.github.jbence1994.erp.common.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
public class FileSystemUtils implements FileUtils {

    @Override
    public void store(String path, String fileName, InputStream stream) throws IOException {
        var targetPath = Paths.get(path);

        if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        Files.copy(stream, Paths.get(path, fileName));
    }

    @Override
    public byte[] read(String path, String fileName) throws IOException {
        var targetPath = Paths.get(path, fileName);
        return Files.readAllBytes(targetPath);
    }
}
