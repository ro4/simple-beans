package me.ro4.beans.io;

import me.ro4.beans.annotation.Component;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 3, time = 2)
@Threads(2)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@SuppressWarnings("unused")
public class ClassParserBenchmark {

//    public static class Shared {
//        public ClassParser reflectionParser;
//        public ClassParser asmParser;
//    }
//
//    @State(Scope.Benchmark)
//    public static class PrototypeCreationState extends Shared {
//        @Setup
//        public void setup() throws ClassNotFoundException, IOException {
//            String obj = "me.ro4.beans.beanexample.AnnoBean";
//            reflectionParser = new ReflectionClassParser(obj);
//            asmParser = new AsmClassParser(obj);
//        }
//    }

    /**
     * Benchmark                         Mode  Cnt     Score     Error   Units
     * ClassParserBenchmark.asm         thrpt    6    32.691 ±   1.060  ops/ms
     * ClassParserBenchmark.reflection  thrpt    6  3130.619 ± 134.577  ops/ms
     */
    @Benchmark
    public Object asm() throws IOException {
        String obj = "me.ro4.beans.beanexample.AnnoBean";
        ClassParser parser = new AsmClassParser(obj);
        return parser.hasAnnotation(Component.class);
    }

    @Benchmark
    public Object reflection() throws ClassNotFoundException {
        String obj = "me.ro4.beans.beanexample.AnnoBean";
        ClassParser parser = new ReflectionClassParser(obj);
        return parser.hasAnnotation(Component.class);
    }
}
