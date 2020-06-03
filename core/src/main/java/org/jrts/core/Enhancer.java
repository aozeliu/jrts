package org.jrts.core;

import java.util.Set;

public interface Enhancer {

    /**
     * 增强字节码
     *
     * @param srcByteCodeArray 源字节码数组
     * @return 增强后的字节码数组
     */
    byte[] transform(String classname, byte[] srcByteCodeArray);
}
