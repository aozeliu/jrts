package org.jrts.core.enhance;

import org.jrts.core.util.Types;
import org.jrts.monitor.Monitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.Method;


/**
 * TODO 减少插桩点
 */
public class MethodEnhancer extends AdviceAdapter {

    private String classname;

    public MethodEnhancer(String classname, MethodVisitor mv, int access, String name, String desc) {
        super(ASM7, mv, access, name, desc);
        this.classname = classname;
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        int access = getAccess();
        // 非私有的静态方法
        // 非私有的构造函数
        if ((access & ACC_PRIVATE) == 0
                && ((access & ACC_STATIC) != 0 || getName().equals("<init>"))) {
            insertMonitorMethodOnBefore();
        }
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
        if(!Types.isArrayType(owner) && !Types.isIgnorableClass(Types.getClassnameFromInternalName(owner))&& (opcode == INVOKEINTERFACE || opcode == INVOKEVIRTUAL)){
            String ownerClassname = Types.getClassnameFromInternalName(owner);
            if(!Types.isIgnorableClass(ownerClassname)) {
                insertMonitorMethodOnCallBefore(ownerClassname);
            }
        }
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        if(Types.isClassType(descriptor) && (opcode == GETSTATIC /*|| opcode == PUTSTATIC*/)){
            // 对于静态字段的访问，目前记录两个信息，
            //          一个字段所在类的信息
            //          一个是字段的定义信息
            //          字段的实际类型和定义类型可能不相同
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


    @Override
    public void visitLdcInsn(Object value) {
        if(value instanceof Type){
            Type type = (Type)value;
            String className = type.getClassName();
            if(!Types.isIgnorableClass(className) && type.getSort() == Type.OBJECT){
                insertMonitorMethodOnCallBefore(className);
            }
        }
        super.visitLdcInsn(value);
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
