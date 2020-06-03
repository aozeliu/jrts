package org.jrts.core;

import org.apache.commons.io.FileUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class EnhancerImpl implements Enhancer{

    private static final Logger logger = LoggerFactory.getLogger(EnhancerImpl.class);

    @Override
    public byte[] transform(String classname, byte[] srcByteCodeArray) {
        ClassReader classReader = new ClassReader(srcByteCodeArray);
        ClassWriter classWriter = new ClassWriter(classReader,
                ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        MonitorWeaver monitorWeaver = new MonitorWeaver(classname, classWriter);
        classReader.accept(monitorWeaver, ClassReader.EXPAND_FRAMES);
        return dumpIfNecessary(classReader.getClassName(), classWriter.toByteArray());
    }

    private static final boolean isDumpClass = false;

    /*
     * dump 字节码
     */
    private static byte[] dumpIfNecessary(String className, byte[] data) {
        if (!isDumpClass) {
            return data;
        }
        final File dumpClassFile = new File("./jrts-class-dump/" + className + ".class");
        final File classPath = new File(dumpClassFile.getParent());

        // 创建类所在的包路径
        if (!classPath.mkdirs()
                && !classPath.exists()) {
            logger.warn("create dump classpath={} failed.", classPath);
            return data;
        }

        // 将类字节码写入文件
        try {
            FileUtils.writeByteArrayToFile(dumpClassFile, data);
            logger.info("dump {} to {} success.", className, dumpClassFile);
        } catch (IOException e) {
            logger.warn("dump {} to {} failed.", className, dumpClassFile, e);
        }

        return data;
    }
}
