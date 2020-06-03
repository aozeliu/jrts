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
        if(Types.isClassType(descriptor) && (opcode == GETSTATIC || opcode == PUTSTATIC)){
            String ownerClassname = Types.getClassnameFromInternalName(owner);
            String fieldClassname = Types.getClassnameFromDescriptor(descriptor);
            if(Types.isIgnorableClass(ownerClassname)){
                ownerClassname = null;
            }
            if (Types.isIgnorableClass(fieldClassname)) {
                fieldClassname = null;
            }
            if(ownerClassname != null || fieldClassname != null) {
                insertMonitorMethodOnStaticAccess(ownerClassname, fieldClassname);
            }
        }
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }


    private void insertMonitorMethodOnBefore(){
        try {
            push(Type.getType(Types.getDescriptorFromClassname(classname)));
            loadThisOrPushNullIfIsStatic();
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnBefore", Class.class, Object.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void insertMonitorMethodOnCallBefore(String classname){
        try {
            push(Type.getType(Types.getDescriptorFromClassname(classname)));
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnCallBefore", Class.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void insertMonitorMethodOnStaticAccess(String ownerClassname, String fieldClassName){
        try {

            push(ownerClassname == null ? null : Type.getType(Types.getDescriptorFromClassname(ownerClassname)));
            push(fieldClassName == null ? null : Type.getType(Types.getDescriptorFromClassname(fieldClassName)));
            invokeStatic(Type.getType(Monitor.class),
                    Method.getMethod(
                            Monitor.class.getDeclaredMethod(
                                    "monitorMethodOnStaticAccess", Class.class, Class.class)));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }



}
