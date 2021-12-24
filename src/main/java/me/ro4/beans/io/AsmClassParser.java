package me.ro4.beans.io;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class AsmClassParser implements ClassParser {

    private String className;
    private ClassReader classReader;
    private ClassNode classNode;

    public AsmClassParser(String className) throws IOException {
        classReader = new ClassReader(className);
        classNode = new ClassNode();
        classReader.accept(classNode, ClassReader.SKIP_DEBUG);
        this.className = className;
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        if (null != classNode.visibleAnnotations) {
            for (AnnotationNode annotationNode : classNode.visibleAnnotations) {
                if (annotationNode.desc.contains(annotationClass.getName().replace(".", "/"))) {
                    return true;
                }
            }
        }
        if (null != classNode.invisibleAnnotations) {
            for (AnnotationNode annotationNode : classNode.invisibleAnnotations) {
                if (annotationNode.desc.contains(annotationClass.getName().replace(".", "/"))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean methodHasAnnotation(String methodName, Class<? extends Annotation> annotationClass) {
        return false;
    }
}
