package org.example.a_bean.b_implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.io.ClassPathResource;

import java.util.Objects;

/**
 * 容器实现
 */
@Slf4j
public class A02Application {
    public static void main(String[] args) {
        beanFactory();
        log.info("********************************************");
        applicationContext();
    }

    /**
     * {@link DefaultListableBeanFactory} 是 {@link BeanFactory} 最重要的实现
     * <p>
     * 一、{@link BeanFactory} 不会做的事、{@link ApplicationContext}会做
     * 1、不会主动调用 {@link BeanPostProcessor}、{@link BeanFactoryPostProcessor}
     * 2、不会主动初始化单例
     * 3、不会解析 ${} 、 #{}
     * 二、排序
     * 默认:{@link AutowiredAnnotationBeanPostProcessor} 比 {@link CommonAnnotationBeanPostProcessor} 优先级高
     * 若添加了比较器则
     * {@link CommonAnnotationBeanPostProcessor} 的 {@link Ordered#getOrder()} 比 {@link AutowiredAnnotationBeanPostProcessor} 小
     */
    private static void beanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // bean 定义(class、scope、初始化、销毁)
        AbstractBeanDefinition definition = BeanDefinitionBuilder.genericBeanDefinition(BeanFactoryConfig.class)
                .setScope("singleton").getBeanDefinition();

        beanFactory.registerBeanDefinition("config", definition);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("{}", beanDefinitionName);
        }

        log.info("==========================================");

        // 给 beanFactory 添加常用的后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("{}", beanDefinitionName);
        }

        // 执行 beanFactory 后处理器,主要作用:补充一些 bean 的定义
        beanFactory.getBeansOfType(BeanFactoryPostProcessor.class).values()
                .forEach(beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory));

        log.info("==========================================");

        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("{}", beanDefinitionName);
        }

        log.info("==========================================");

        //log.info("bean2:{}", beanFactory.getBean(Bean1.class).getBean2());

        log.info("==========================================");

        // 执行 bean 后处理器,主要作用:针对 bean 的生命周期的各个阶段提供扩展
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().stream()
                // 添加比较器 根据 order 值 排序
                .sorted(Objects.requireNonNull(beanFactory.getDependencyComparator()))
                .forEach(beanPostProcessor -> {
                    log.info("beanPostProcessor:{}", beanPostProcessor);
                    beanFactory.addBeanPostProcessor(beanPostProcessor);
                });

        // 准备好所有的单例
        beanFactory.preInstantiateSingletons();

        log.info("==========================================");

        Bean1 bean1 = beanFactory.getBean(Bean1.class);

        log.info("bean2:{}", bean1.getBean2());

        log.info("inter:{}", bean1.getInter());
    }

    private static void applicationContext() {
        classPathXmlApplicationContext();
        log.info("==========================================");
        fileSystemXmlApplicationContext();
        log.info("==========================================");
        principle();
        log.info("==========================================");
        annotationConfigApplicationContext();
        log.info("==========================================");
        annotationConfigServletWebServerApplicationContext();
    }

    private static void classPathXmlApplicationContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("b01.xml");
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
    }

    private static void fileSystemXmlApplicationContext() {
        FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/b01.xml");
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
    }

    /**
     * {@link ClassPathXmlApplicationContext} 与 {@link FileSystemXmlApplicationContext} 原理
     * 底层是{@link DefaultListableBeanFactory}
     */
    private static void principle() {
        log.info("after...");
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(new ClassPathResource("b01.xml"));
        log.info("before...");
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
    }

    private static void annotationConfigApplicationContext() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ApplicationContextConfig.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
    }

    private static void annotationConfigServletWebServerApplicationContext() {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);
        for (String beanDefinitionName : context.getBeanDefinitionNames()) {
            log.info("beanDefinitionName:{}", beanDefinitionName);
        }
    }

}
