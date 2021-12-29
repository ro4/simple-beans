package me.ro4.beans.io;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.lang.annotation.Annotation;

/**
 * @see ClassParserBenchmark
 * 速度比反射慢了 100 倍，可能没用对，先用反射的方式来
 */
public class AsmClassParser implements ClassParser {

    private static final int PARSING_OPTIONS = ClassReader.SKIP_DEBUG
            | ClassReader.SKIP_CODE | ClassReader.SKIP_FRAMES;

    private final ClassReader classReader;
    private final ClassNode classNode;

    public AsmClassParser(String className) throws IOException {
        classReader = new ClassReader(className);
        classNode = new ClassNode();
        classReader.accept(classNode, PARSING_OPTIONS);
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
