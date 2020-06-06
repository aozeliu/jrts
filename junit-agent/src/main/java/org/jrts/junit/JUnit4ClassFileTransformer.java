package org.jrts.junit;

import org.jrts.core.util.Utils;
import org.jrts.monitor.junit.JUnit4Monitor;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class JUnit4ClassFileTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (className.equals("org/junit/runners/ParentRunner")){
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            JUnit4ClassVisitor jUnitClassVisitor = new JUnit4ClassVisitor(classWriter);
            classReader.accept(jUnitClassVisitor, ClassReader.EXPAND_FRAMES);
            return Utils.dumpIfNecessary(classReader.getClassName(), classWriter.toByteArray());
        }
        return null;
    }

    static class JUnit4ClassVisitor extends ClassVisitor {

        JUnit4ClassVisitor(ClassVisitor classVisitor) {
            super(Opcodes.ASM7, classVisitor);
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, descriptor, signature, exceptions);
            if("run".equals(name) && descriptor.equals("(Lorg/junit/runner/notification/RunNotifier;)V")){
                return new JUnit4MethodVisitor(methodVisitor, access, name, descriptor);
            }
            return methodVisitor;
        }
    }

    static final Type JUnit4MonitorType = Type.getType(JUnit4Monitor.class);

    static class JUnit4MethodVisitor extends AdviceAdapter{
        JUnit4MethodVisitor(MethodVisitor methodVisitor, int access, String name, String descriptor) {
            super(ASM7, methodVisitor, access, name, descriptor);
        }

        private Label label = new Label();

        @Override
        protected void onMethodEnter() {
            try {
                loadThis();
                invokeStatic(
                        JUnit4MonitorType,
                        Method.getMethod(JUnit4Monitor.class.getDeclaredMethod("runBefore", Object.class))
                );
                ifZCmp(NE, label);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onMethodExit(int opcode) {
            try {
                mark(label);
                loadThis();
                invokeStatic(
                        JUnit4MonitorType,
                        Method.getMethod(JUnit4Monitor.class.getDeclaredMethod("runAfter", Object.class))
                );
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
