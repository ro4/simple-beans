JDK 里面有个 ASM 库，可用来遍历代码，和 Spring Core Asm 下面作用应该差不多。


Spring 使用 ASM 来读取类信息，但是我自己的 benchmark 表示 ASM 读取比较慢，明天再测试下；
Spring ASM 读取类信息地址【SimpleMetadataReader】。

读取类的调用栈
```
Full thread dump
"main@1" prio=5 tid=0x1 nid=NA runnable
java.lang.Thread.State: RUNNABLE
at org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.scanCandidateComponents(ClassPathScanningCandidateComponentProvider.java:429)
at org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider.findCandidateComponents(ClassPathScanningCandidateComponentProvider.java:316)
at org.springframework.context.annotation.ClassPathBeanDefinitionScanner.doScan(ClassPathBeanDefinitionScanner.java:276)
at org.springframework.context.annotation.ComponentScanAnnotationParser.parse(ComponentScanAnnotationParser.java:128)
at org.springframework.context.annotation.ConfigurationClassParser.doProcessConfigurationClass(ConfigurationClassParser.java:296)
at org.springframework.context.annotation.ConfigurationClassParser.processConfigurationClass(ConfigurationClassParser.java:250)
at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:207)
at org.springframework.context.annotation.ConfigurationClassParser.parse(ConfigurationClassParser.java:175)
at org.springframework.context.annotation.ConfigurationClassPostProcessor.processConfigBeanDefinitions(ConfigurationClassPostProcessor.java:331)
at org.springframework.context.annotation.ConfigurationClassPostProcessor.postProcessBeanDefinitionRegistry(ConfigurationClassPostProcessor.java:247)
at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanDefinitionRegistryPostProcessors(PostProcessorRegistrationDelegate.java:311)
at org.springframework.context.support.PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(PostProcessorRegistrationDelegate.java:112)
at org.springframework.context.support.AbstractApplicationContext.invokeBeanFactoryPostProcessors(AbstractApplicationContext.java:746)
at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:564)
- locked <0x11f1> (a java.lang.Object)
at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:145)
at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:730)
at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:412)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:302)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1301)
at org.springframework.boot.SpringApplication.run(SpringApplication.java:1290)
at me.ro4.basic.BasicApplication.main(BasicApplication.java:11)
```
