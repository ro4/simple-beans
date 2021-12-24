package me.ro4.beans.io;

import me.ro4.beans.annotation.Bean;
import me.ro4.beans.annotation.Component;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class ClassParserTest {

    @Test
    public void test() throws ClassNotFoundException, NoSuchMethodException, IOException {
        String obj = "me.ro4.beans.beanexample.AnnoBean";
        ReflectionClassParser reflectionParser = new ReflectionClassParser(obj);
        Assert.assertTrue(reflectionParser.hasAnnotation(Component.class));
        Assert.assertFalse(reflectionParser.hasAnnotation(Bean.class));
        Assert.assertTrue(reflectionParser.methodHasAnnotation("hello", Bean.class));
        Assert.assertFalse(reflectionParser.methodHasAnnotation("hello", Component.class));

        AsmClassParser asmParser = new AsmClassParser(obj);
        Assert.assertTrue(asmParser.hasAnnotation(Component.class));
        Assert.assertFalse(asmParser.hasAnnotation(Bean.class));
    }
}
