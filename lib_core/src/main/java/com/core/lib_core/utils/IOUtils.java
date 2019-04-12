package com.core.lib_core.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author 李澄锋<br>
 * 2018/12/27
 */
public class IOUtils {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
