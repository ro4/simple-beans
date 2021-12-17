package me.ro4.beans;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.*;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3)
@Measurement(iterations = 3, time = 2, timeUnit = TimeUnit.SECONDS)
@Threads(2)
@Fork(2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class FirstBenchmark {

    @Benchmark
    public void testStringAdd() {
        String a = "";
        for (int i = 0; i < 10; i++) {
            a += i;
        }
        // System.out.println(a);
    }

    @Benchmark
    public void testStringBuilderAdd() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(i);
        }
        // System.out.println(sb.toString());
    }

    public static void main(String[] args) throws RunnerException {
        System.out.println("hello");
        Options options = new OptionsBuilder()
                .include(FirstBenchmark.class.getSimpleName())
//                .output("target/jmh.log")
                .build();
        new Runner(options).run();
    }

}