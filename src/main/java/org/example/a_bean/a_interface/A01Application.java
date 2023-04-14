package org.example.a_bean.a_interface;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnvironmentCapable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableAsync;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

/**
 * 容器接口
 * 1、什么是 {@link BeanFactory}
 * {@link BeanFactory} 是 {@link ApplicationContext} 的父接口
 * {@link BeanFactory} 是 Spring 的核心容器,{@link ApplicationContext} 的实现都「组合」了它的功能
 * <p>
 * 2、{@link BeanFactory} 功能
 * 控制反转、基本地依赖注入、直至 Bean 的生命周期的各种功能,都由它的实现类提供
 * {@link DefaultSingletonBeanRegistry#singletonObjects} 包含所有的单例bean
 */
@Slf4j
@EnableAsync
@SpringBootApplication
@SuppressWarnings("JavadocReference")
public class A01Application {
    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(A01Application.class, args);
        beanFactory(context);
        applicationContext(context);
    }

    @SneakyThrows
    @SuppressWarnings({"unchecked"})
    private static void beanFactory(ConfigurableApplicationContext context) {
        Field singletonObjectsField = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        singletonObjectsField.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        Map<String, Object> map = (Map<String, Object>) singletonObjectsField.get(beanFactory);
        map.forEach((k, v) -> log.info("{}==>{}", k, v));
    }

    /**
     * {@link ApplicationContext} 比 {@link BeanFactory} 多点啥
     */
    private static void applicationContext(ConfigurableApplicationContext context) {
        messageSource(context);
        resourcePatternResolver(context);
        environmentCapable(context);
        eventPublisher(context);
    }

    /**
     * 国际化
     * {@link MessageSource}
     */
    private static void messageSource(ConfigurableApplicationContext context) {
        log.info("CHINA:{}", context.getMessage("hi", null, Locale.CHINA));
        log.info("ENGLISH:{}", context.getMessage("hi", null, Locale.ENGLISH));
        log.info("JAPAN:{}", context.getMessage("hi", null, Locale.JAPAN));
    }

    /**
     * 解析通配符并获取相应资源
     * {@link ResourcePatternResolver}
     */
    @SneakyThrows
    private static void resourcePatternResolver(ConfigurableApplicationContext context) {
        Resource[] resources = context.getResources("classpath:application.yaml");
        for (Resource resource : resources) {
            log.info("{}", resource);
        }
        resources = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources) {
            log.info("{}", resource);
        }
    }

    /**
     * 获取配置信息
     * {@link EnvironmentCapable}
     */
    private static void environmentCapable(ConfigurableApplicationContext context) {
        ConfigurableEnvironment environment = context.getEnvironment();
        log.info("javaHome:{}", environment.getProperty("java.home"));
        log.info("port:{}", environment.getProperty("server.port"));
    }

    /**
     * 发布事件
     * {@link ApplicationEventPublisher}
     */
    private static void eventPublisher(ConfigurableApplicationContext context) {
        context.publishEvent(new Event(context));
    }
}
