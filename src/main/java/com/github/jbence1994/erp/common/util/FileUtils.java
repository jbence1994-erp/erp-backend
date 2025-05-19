package com.github.jbence1994.erp.common.util;

import java.io.IOException;
import java.io.InputStream;

public interface FileUtils {
    void store(String path, String fileName, InputStream stream) throws IOException;

    byte[] read(String path, String fileName) throws IOException;

    void delete(String path, String fileName) throws IOException;
}
