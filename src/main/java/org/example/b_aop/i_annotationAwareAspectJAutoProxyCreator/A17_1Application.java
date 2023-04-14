package org.example.b_aop.i_annotationAwareAspectJAutoProxyCreator;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

/**
 * 创建 --> (*) 依赖注入 --> 初始化 (*)
 * 代理对象创建时机: 依赖注入之前/初始化之后 二选一
 * 无循环依赖 则在初始化之后创建
 * 有循环依赖 则在依赖注入之前创建 并暂存于二级缓存
 * 依赖注入与初始化不应该被增强,仍应被施加于原始对象
 */
@Slf4j
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class A17_1Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(Config.class);
        context.refresh();
        context.close();
    }

    @Configuration
    static class Config {
        /**
         * 解析 {@link Aspect} {@link Advisor} 将高级切面转为低级切面并创建代理
         */
        @Bean
        public AnnotationAwareAspectJAutoProxyCreator annotationAwareAspectJAutoProxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        /**
         * 解析 {@link Autowired}
         */
        @Bean
        public AutowiredAnnotationBeanPostProcessor autowiredAnnotationBeanPostProcessor() {
            return new AutowiredAnnotationBeanPostProcessor();
        }

        /**
         * 解析 {@link PostConstruct}
         */
        @Bean
        public CommonAnnotationBeanPostProcessor commonAnnotationBeanPostProcessor() {
            return new CommonAnnotationBeanPostProcessor();
        }

        @Bean
        public Advisor advisor(MethodInterceptor advice) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            return new DefaultPointcutAdvisor(pointcut, advice);
        }

        @Bean
        public MethodInterceptor advice() {
            return invocation -> {
                log.info("before...");
                Object proceed = invocation.proceed();
                log.info("after...");
                return proceed;
            };
        }

        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }

    }

    static class Bean1 {

        public Bean1() {
            log.info("Bean1()");
        }

        @Autowired
        public void setBean2(Bean2 bean2) {
            log.info("Bean1 setBean2(bean2):{}", bean2.getClass());
        }

        public void foo() {
        }

        @PostConstruct
        public void init() {
            log.info("Bean1 init()");
        }
    }

    static class Bean2 {
        public Bean2() {
            log.info("Bean2()");
        }

        @Autowired
        public void setBean1(Bean1 bean1) {
            log.info("Bean2 setBean1(bean1):{}", bean1.getClass());
        }

        @PostConstruct
        public void init() {
            log.info("Bean2 init()");
        }
    }
}
