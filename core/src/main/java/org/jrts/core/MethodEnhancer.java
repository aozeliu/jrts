package org.jrts.core;

import org.jrts.monitor.Monitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;

public class MethodEnhancer extends AdviceAdapter {

    private String classname;

    public MethodEnhancer(String classname, MethodVisitor mv, int access, String name, String desc) {
        super(ASM7, mv, access, name, desc);
        this.classname = classname;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        insertMonitorMethodOnBefore();
    }

    protected void loadThisOrPushNullIfIsStatic() {
        if (isStaticMethod()) {
            push((Type)null);
        } else {
            loadThis();
        }
    }

    protected boolean isStaticMethod() {
        return (methodAccess & ACC_STATIC) != 0;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if(opcode == INVOKEINTERFACE || opcode == INVOKEVIRTUAL) {
            String ownerClassname = Types.getClassnameFromInternalName(owner);
            if(!Types.isIgnorableClass(ownerClassname)) {
                insertMonitorMethodOnCallBefore(ownerClassname);
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if(opcode == GETSTATIC || opcode == PUTSTATIC) {
            String ownerClassname = Types.getClassnameFromInternalName(owner);
            String fieldClassname = Types.getClassnameFromDescriptor(descriptor);
            if(!Types.isIgnorableClass(ownerClassname)
                    || !Types.isIgnorableClass(fieldClassname)){
                insertMonitorMethodOnStaticAccess(ownerClassname, fieldClassname);
            }
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }


    private void insertMonitorMethodOnBefore(){
        try {
            push(classname);
            loadThisOrPushNullIfIsStatic();
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnBefore", String.class, Object.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void insertMonitorMethodOnCallBefore(String classname){
        try {
            push(classname);
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnCallBefore", String.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void insertMonitorMethodOnStaticAccess(String ownerClassname, String fieldClassName){
        try {
            push(ownerClassname);
            push(fieldClassName);
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnStaticAccess", String.class, String.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }



}
