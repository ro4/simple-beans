package me.ro4.beans.io;

import org.objectweb.asm.*;

import java.io.IOException;

class ClassPrinter extends ClassVisitor {
    public ClassPrinter() {
        super(Opcodes.ASM4);
    }

    public void visit(
            int version,
            int access,
            String name,
            String signature,
            String superName,
            String[] interfaces
    ) {
        System.out.println(name + " extends " + superName + " {");
    }

    public void visitSource(String source, String debug) {
        System.out.println("source " + source + ", " + "debug " + debug);
    }

    public void visitOuterClass(String owner, String name, String desc) {
    }

    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        System.out.println(desc);
        System.out.println(visible);
        return null;
    }

    public void visitAttribute(Attribute attr) {
    }

    public void visitInnerClass(String name, String outerName,
                                String innerName, int access) {
    }

    public FieldVisitor visitField(
            int access,
            String name,
            String desc,
            String signature,
            Object value
    ) {
        System.out.println(" " + desc + " " + name);
        return null;
    }

    public MethodVisitor visitMethod(
            int access,
            String name,
            String desc,
            String signature,
            String[] exceptions
    ) {
        System.out.println(" " + name + desc);
        return null;
    }

    public void visitEnd() {
        System.out.println("}");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String clzName = "me.ro4.beans.io.SimpleResourcePatternResolver";
        ClassPrinter cp = new ClassPrinter();
        ClassReader cr = new ClassReader(clzName);
        cr.accept(cp, 0);

        System.out.println("asm finished");
        Class<?> clazz = Class.forName(clzName);
    }
}