# WHY Spring 使用 ASM 读取类信息而不是 Reflection?

## 名词解释
### ASM
`ASM` is an all purpose Java bytecode manipulation and analysis framework. It can be used to modify existing classes or to dynamically generate classes, directly in binary form. ASM provides some common bytecode transformations and analysis algorithms from which custom complex transformations and code analysis tools can be built. ASM offers similar functionality as other Java bytecode frameworks, but is focused on performance. Because it was designed and implemented to be as small and as fast as possible, it is well suited for use in dynamic systems (but can of course be used in a static way too, e.g. in compilers).

ASM is used in many projects, including:
* the OpenJDK, to generate the lambda call sites, and also in the Nashorn compiler,
* the Groovy compiler and the Kotlin compiler,
* Cobertura and Jacoco, to instrument classes in order to measure code coverage,
* CGLIB, to dynamically generate proxy classes (which are used in other projects such as Mockito and EasyMock),
* Gradle, to generate some classes at runtime.

`ASM` 是一个全功能的 Java 字节码操作分析框架。ASM 能直接修改二进制字节码，从而修改已存在的类或者动态生成类。ASM 内置了一些通用的字节码转换和分析算法，以便于用户定制自己复杂的代码转换和分析工具。ASM 提供的功能和其他 Java 字节码框架基本上一致，但是更专注于性能，因为 ASM 尽可能实现得小巧而快速，所以能很好的适配动态系统（当让也能用于静态系统，比如编译器）。

很多项目在使用 ASM，包括：
* OpenJDK：用于生成 lambda call sites、Nashorn 编译器
* Groovy 编译器、Kotlin 编译器
* Cobertura 和 Jacoco：用于测量代码覆盖情况
* CGLIB：用于动态生成代理对象（被使用在 Mockito、EasyMock 等项目中）
* Gradle：用于运行时生成类

### Reflection
`Reflection` is a feature in the Java programming language. It allows an executing Java program to examine or "introspect" upon itself, and manipulate internal properties of the program. For example, it's possible for a Java class to obtain the names of all its members and display them.

`反射` 是 Java 编程语言的一个功能，它能实现 Java 程序自己检查自己或者“内省”，还能操作程序内部属性。比如，反射能够让一个 Java 类获取它内部的所有成员并展示出来。

## References：
1. [Using Java Reflection](https://www.oracle.com/technical-resources/articles/java/javareflection.html)
2. [ASM home page](https://asm.ow2.io)
