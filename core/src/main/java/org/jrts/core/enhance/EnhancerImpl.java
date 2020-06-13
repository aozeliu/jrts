package org.jrts.core.enhance;

import lombok.extern.slf4j.Slf4j;
import org.jrts.core.util.Utils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

@Slf4j
public class EnhancerImpl implements Enhancer {


    @Override
    public byte[] transform(String classname, byte[] srcByteCodeArray) {
        ClassReader classReader = new ClassReader(srcByteCodeArray);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
        MonitorWeaver monitorWeaver = new MonitorWeaver(classname, classWriter);
        classReader.accept(monitorWeaver, ClassReader.EXPAND_FRAMES);
        return Utils.dumpIfNecessary(classReader.getClassName(), classWriter.toByteArray());
    }
}
