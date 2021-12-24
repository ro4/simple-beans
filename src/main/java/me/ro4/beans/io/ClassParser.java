package me.ro4.beans.io;

import java.lang.annotation.Annotation;

public interface ClassParser {

    boolean hasAnnotation(Class<? extends Annotation> annotationClass);

    boolean methodHasAnnotation(String methodName, Class<? extends Annotation> annotationClass) throws NoSuchMethodException;
}
