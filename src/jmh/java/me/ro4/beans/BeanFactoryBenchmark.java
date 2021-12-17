package me.ro4.beans;

import me.ro4.beans.beanexample.SimpleBean;
import me.ro4.beans.impl.SimpleBeanDefinition;
import me.ro4.beans.impl.SimpleBeanFactory;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 3, time = 2)
@Threads(2)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@SuppressWarnings("unused")
public class BeanFactoryBenchmark {

    public static class Shared {
        public BeanFactory beanFactory;
    }

    @State(Scope.Benchmark)
    public static class PrototypeCreationState extends Shared {
        @Setup
        public void setup() {
            beanFactory = new SimpleBeanFactory();
            BeanDefinition beanDefinition = new SimpleBeanDefinition();
            beanDefinition.setClassName(SimpleBean.class.getName());
            beanFactory.registerBeanDefinition("sb", beanDefinition);
        }
    }

    @Benchmark
    public Object getBeanByName(PrototypeCreationState state) {
        return state.beanFactory.getBean("sb");
    }

    @Benchmark
    public Object getBeanByNameAndType(PrototypeCreationState state) {
        return state.beanFactory.getBean("sb", SimpleBean.class);
    }
}
