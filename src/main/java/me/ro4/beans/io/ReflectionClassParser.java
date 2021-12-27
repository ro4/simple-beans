package me.ro4.beans.io;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class ReflectionClassParser implements ClassParser {

    private final Class<?> clazz;

    public ReflectionClassParser(String className) throws ClassNotFoundException {
        this.clazz = Class.forName(className);
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    @Override
    public boolean methodHasAnnotation(String methodName, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        Method method = clazz.getDeclaredMethod(methodName);
        return method.isAnnotationPresent(annotationClass);
    }
}
