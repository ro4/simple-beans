package me.ro4.beans;

import me.ro4.beans.beanexample.*;
import me.ro4.beans.impl.SimpleBeanDefinition;
import me.ro4.beans.impl.SimpleBeanFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BeanFactoryAutowiredTest {
    BeanFactory beanFactory;

    @Before
    public void setUp() {
        this.beanFactory = new SimpleBeanFactory();
        beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

    @Test
    public void autowiredFieldTest() {
        BeanDefinition beanDefinition = new SimpleBeanDefinition();
        beanDefinition.setClassName(SimpleBean.class.getName());
        beanFactory.registerBeanDefinition("sb", beanDefinition);

        BeanDefinition personBD = new SimpleBeanDefinition();
        personBD.setClassName(PersonBean.class.getName());
        beanFactory.registerBeanDefinition("person", personBD);

        PersonBean pb = beanFactory.getBean("person", PersonBean.class);
        Assert.assertNotNull(pb);
        Assert.assertNotNull(pb.getName());
        Assert.assertNotNull(pb.getAge());
        Assert.assertNotNull(pb.getFoot());
    }

    @Test
    public void singletonCircularReferenceTest() {
        BeanDefinition fatherDefinition = new SimpleBeanDefinition();
        fatherDefinition.setClassName(FatherBean.class.getName());
        beanFactory.registerBeanDefinition("father", fatherDefinition);

        BeanDefinition sonDefinition = new SimpleBeanDefinition();
        sonDefinition.setClassName(SonBean.class.getName());
        beanFactory.registerBeanDefinition("son", sonDefinition);

        FatherBean father = beanFactory.getBean("father", FatherBean.class);
        SonBean son = beanFactory.getBean("son", SonBean.class);

        Assert.assertNotNull(father);
        Assert.assertNotNull(son);
    }

    @Test
    public void prototypeCircularReferenceTest() {
        BeanDefinition fatherDefinition = new SimpleBeanDefinition();
        fatherDefinition.setClassName(FatherBean.class.getName());
        fatherDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("father", fatherDefinition);

        BeanDefinition sonDefinition = new SimpleBeanDefinition();
        sonDefinition.setClassName(SonBean.class.getName());
        sonDefinition.setScope(BeanDefinition.ScopeEnum.PROTOTYPE);
        beanFactory.registerBeanDefinition("son", sonDefinition);
        try {
            beanFactory.getBean("father", FatherBean.class);
        } catch (Exception e) {
            Assert.assertEquals(e.getClass(), UnsupportedOperationException.class);
        }
    }

    @Test
    public void mapAndListFieldTest() {
        BeanDefinition womanBeanDefinition = new SimpleBeanDefinition();
        womanBeanDefinition.setClassName(Woman.class.getName());
        BeanDefinition manBeanDefinition = new SimpleBeanDefinition();
        manBeanDefinition.setClassName(Man.class.getName());
        BeanDefinition multipleBeanDefinition = new SimpleBeanDefinition();
        multipleBeanDefinition.setClassName(MultipleBean.class.getName());
        beanFactory.registerBeanDefinition("woman", womanBeanDefinition);
        beanFactory.registerBeanDefinition("man", manBeanDefinition);
        beanFactory.registerBeanDefinition("multipleBean", multipleBeanDefinition);

        MultipleBean multipleBean = beanFactory.getBean("multipleBean", MultipleBean.class);
        Man man = beanFactory.getBean("man", Man.class);
        Woman woman = beanFactory.getBean("woman", Woman.class);
        Assert.assertNotNull(multipleBean);
        Assert.assertNotNull(multipleBean.getHumanMap());
        Assert.assertEquals("must equal", man, multipleBean.getHumanMap().get("man"));
        Assert.assertEquals("must equal", woman, multipleBean.getHumanMap().get("woman"));
        Assert.assertNotNull(multipleBean.getHumanList());
        Assert.assertEquals("there are two impl", 2, multipleBean.getHumanList().size());
    }
}
