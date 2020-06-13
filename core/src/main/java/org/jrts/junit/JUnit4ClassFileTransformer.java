package org.jrts.junit;

import org.jrts.core.util.Types;
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
        if (JUnit4Types.isJUnit4Runner(Types.getClassnameFromInternalName(className))){
            ClassReader classReader = new ClassReader(classfileBuffer);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES){
                @Override
                protected String getCommonSuperClass(String type1, String type2) {
                    Class<?> class1;
                    try {
                        class1 = Class.forName(type1.replace('/', '.'), false, loader);
                    } catch (ClassNotFoundException e) {
                        throw new TypeNotPresentException(type1, e);
                    }
                    Class<?> class2;
                    try {
                        class2 = Class.forName(type2.replace('/', '.'), false, loader);
                    } catch (ClassNotFoundException e) {
                        throw new TypeNotPresentException(type2, e);
                    }
                    if (class1.isAssignableFrom(class2)) {
                        return type1;
                    }
                    if (class2.isAssignableFrom(class1)) {
                        return type2;
                    }
                    if (class1.isInterface() || class2.isInterface()) {
                        return "java/lang/Object";
                    } else {
                        do {
                            class1 = class1.getSuperclass();
                        } while (!class1.isAssignableFrom(class2));
                        return class1.getName().replace('.', '/');
                    }
                }
            };
            JUnit4ClassVisitor jUnitClassVisitor = new JUnit4ClassVisitor(classWriter);
            try {
                classReader.accept(jUnitClassVisitor, ClassReader.EXPAND_FRAMES);
            }catch (Exception e){
                e.printStackTrace();
            }
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
            super(Opcodes.ASM7, methodVisitor, access, name, descriptor);
        }

        @Override
        protected void onMethodEnter() {
            try {
                loadThis();
                invokeStatic(
                        JUnit4MonitorType,
                        Method.getMethod(JUnit4Monitor.class.getDeclaredMethod("runBefore", Object.class))
                );
                Label label = new Label();
                ifZCmp(EQ, label);
                visitInsn(RETURN);
                mark(label);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onMethodExit(int opcode) {
            try {
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
