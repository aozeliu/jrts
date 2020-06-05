package org.jrts.core;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MonitorWeaver extends ClassVisitor implements Opcodes {

    private String classname;

    public MonitorWeaver(String classname, ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
        this.classname = classname;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        if ((access & ACC_ABSTRACT) == 0
                && (access & ACC_NATIVE) == 0
                && (access & ACC_SYNTHETIC) == 0
                && (access & ACC_BRIDGE) == 0) {
            return new MethodEnhancer(classname, mv, access, name, descriptor);
        }
        return mv;
    }
}
