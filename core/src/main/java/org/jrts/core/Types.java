package org.jrts.core;

import org.objectweb.asm.Type;

public class Types {

    public static boolean isIgnorableClass(String className) {
        return (className.startsWith("java.", 0)
                || className.startsWith("javax.", 0)
                || className.startsWith("jdk.", 0)
                || className.startsWith("sun.", 0)
                || className.startsWith("com.sun.", 0)
                || className.startsWith("com.intellij.")
                || className.startsWith("org.jetbrains.")
                || className.startsWith("org.jrts.", 0));
    }

    public static String getClassnameFromInternalName(String internalName){
        return internalName.replace("/", ".");
    }

    public static String getClassnameFromDescriptor(String descriptor){
        return Type.getType(descriptor).getClassName();
    }
}
