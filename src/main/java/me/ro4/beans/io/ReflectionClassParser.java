package me.ro4.beans.io;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectionClassParser implements ClassParser {

    private String className;

    private Class<?> clazz;

    private Map<String, Method> methodCache = new HashMap<>();

    public ReflectionClassParser(String className) throws ClassNotFoundException {
        this.className = className;
        this.clazz = Class.forName(className);
    }

    @Override
    public boolean hasAnnotation(Class<? extends Annotation> annotationClass) {
        return clazz.isAnnotationPresent(annotationClass);
    }

    @Override
    public boolean methodHasAnnotation(String methodName, Class<? extends Annotation> annotationClass) throws NoSuchMethodException {
        Method method;
        if (methodCache.containsKey(methodName)) {
            method = methodCache.get(methodName);
        } else {
            method = clazz.getDeclaredMethod(methodName);
            methodCache.put(methodName, method);
        }
        return method.isAnnotationPresent(annotationClass);
    }
}
