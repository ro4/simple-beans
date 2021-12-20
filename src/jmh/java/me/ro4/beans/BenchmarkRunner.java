package me.ro4.beans;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class BenchmarkRunner {
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(BeanFactoryBenchmark.class.getSimpleName())
                .output("target/jmh.log")
                .build();
        new Runner(options).run();
    }
}
