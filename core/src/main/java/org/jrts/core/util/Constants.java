package org.jrts.core.util;

public class Constants {

    public static final String DEPENDENCY_FILE_EXTENSION = ".dpi";

    public static final String DEPENDENCY_ENTRY_SPLITTER = "=";

    // 默认在工作目录下的.jrts目前存储测试的依赖信息及其他信息
    public static final String JRTS_DEFAULT_DATA_PATH = ".jrts";

    // 是否导出增强后的字节码
    public static final boolean DUMP_CLASS = false;
    public static final String CLASS_DUMP_DIR_PATH = ".jrts/class-dump/";


    public static final String JRTS_CLASSLOADER_NAME = "org.jrts.agent.JRtsClassLoader";

}
