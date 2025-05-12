package com.github.jbence1994.erp.common.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
public interface FileUtils {
    void store(String path, String fileName, InputStream stream) throws IOException;

    byte[] read(String path, String fileName) throws IOException;
}
