package org.jrts.core;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JRtsClassFileTransformer implements ClassFileTransformer {


    private Enhancer enhancer = new EnhancerImpl();

    @Override
    public byte[] transform(ClassLoader loader, String internalName, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        // 忽略BootStrap类加载器和本工具的自定义的类加载器
        if(internalName == null
                || loader == null
                || loader.getClass().getName().equals("org.jrts.agent.JRtsClassLoader")){
            return null;
        }

        String classname = Types.getClassnameFromInternalName(internalName);
        if(Types.isIgnorableClass(classname)){
            return null;
        }
        return enhancer.transform(classname, classfileBuffer);
    }


}
