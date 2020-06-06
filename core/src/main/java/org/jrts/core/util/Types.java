package org.jrts.core.util;

import org.objectweb.asm.Type;

public class Types {

    public static boolean isIgnorableClass(String className) {
        return (className.contains("$Proxy")
                || className.startsWith("java.", 0)
                || className.startsWith("javax.", 0)
                || className.startsWith("jdk.", 0)
                || className.startsWith("sun.", 0)
                || className.startsWith("com.sun.", 0)
                || className.startsWith("org.xml.sax", 0)
                || className.startsWith("org.apache.maven.", 0)
                || className.startsWith("com.intellij.", 0)
                || className.startsWith("org.jetbrains.", 0)
                || className.startsWith("org.junit.", 0)
                || className.startsWith("junit.", 0)
                || className.startsWith("org.jrts.", 0));
    }

    public static boolean isClassType(String descriptor) {
        return descriptor.startsWith("L");
    }

    public static boolean isArrayType(String descriptor){
        return descriptor.startsWith("[");
    }

    public static String getClassnameFromInternalName(String internalName){
        return internalName.replace("/", ".");
    }

    public static String getClassnameFromDescriptor(String descriptor){
        return Type.getType(descriptor).getClassName();
    }

    public static String getDescriptorFromClassname(String classname){
        if(classname == null){
            return null;
        }else{
            return "L" + classname.replace(".", "/") + ";";
        }
    }
}
