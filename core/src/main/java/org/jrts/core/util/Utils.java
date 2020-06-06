package org.jrts.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public class Utils {

    /*
     * dump 字节码
     */
    public static byte[] dumpIfNecessary(String className, byte[] data) {
        if (!Constants.DUMP_CLASS) {
            return data;
        }
        final File dumpClassFile = new File(Constants.CLASS_DUMP_DIR_PATH + className + ".class");
        final File classPath = new File(dumpClassFile.getParent());

        // 创建类所在的包路径
        if (!classPath.mkdirs()
                && !classPath.exists()) {
            log.warn("create dump classpath={} failed.", classPath);
            return data;
        }

        // 将类字节码写入文件
        try {
            FileUtils.writeByteArrayToFile(dumpClassFile, data);
            log.info("dump {} to {} success.", className, dumpClassFile);
        } catch (IOException e) {
            log.warn("dump {} to {} failed.", className, dumpClassFile, e);
        }

        return data;
    }
}
