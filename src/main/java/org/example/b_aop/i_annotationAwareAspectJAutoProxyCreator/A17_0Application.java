package org.example.b_aop.i_annotationAwareAspectJAutoProxyCreator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link AnnotationAwareAspectJAutoProxyCreator} 后处理器
 * 根据目标类型找到 可以匹配到该类中方面所有的切面并把高级切面转为低级切面
 * {@link AbstractAdvisorAutoProxyCreator#findEligibleAdvisors(Class, String)}
 * <p>
 * 创建代理类
 * {@link AbstractAutoProxyCreator#wrapIfNecessary(Object, String, Object)}
 * <p>
 * {@link Bean} 上加{@link Order} 不生效,在方法上的 {@link Order} 不生效
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class A17_0Application {
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("aspect", Aspect1.class);
        context.registerBean("config", Config.class);
        context.registerBean(ConfigurationClassPostProcessor.class);

        context.registerBean(AnnotationAwareAspectJAutoProxyCreator.class);

        context.refresh();
        for (String name : context.getBeanDefinitionNames()) {
            log.info("name:{}", name);
        }

        parseAspectAndAdvisor(context);
    }

    /**
     * 1、根据目标类型找到 可以匹配到该类中方面所有的切面,把高级切面转为低级切面
     * 2、根据切面创建代理对象
     */
    @SneakyThrows
    private static void parseAspectAndAdvisor(GenericApplicationContext context) {
        AbstractAdvisorAutoProxyCreator creator = context.getBean(AbstractAdvisorAutoProxyCreator.class);

        getAdvisors(creator);

        wrapIfNecessary(creator);
    }

    /**
     * 根据目标类型找到 可以匹配到该类中方面所有的切面并把高级切面转为低级切面
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    private static void getAdvisors(AbstractAdvisorAutoProxyCreator creator) {

        Method findEligibleAdvisorsMethod = AbstractAdvisorAutoProxyCreator.class.getDeclaredMethod("findEligibleAdvisors", Class.class, String.class);
        findEligibleAdvisorsMethod.setAccessible(true);
        List<Advisor> advisors = (List<Advisor>) findEligibleAdvisorsMethod.invoke(creator, Target1.class, "target1");

        for (Advisor advisor : advisors) {
            log.info("tag1Advisors:{}", advisor);
        }
    }

    /**
     * 内部会调用{@link AbstractAdvisorAutoProxyCreator#findEligibleAdvisors(Class, String)}
     * 若返回值不为空,则创建并返回代理类
     */
    @SneakyThrows
    private static void wrapIfNecessary(AbstractAdvisorAutoProxyCreator creator) {

        Method wrapIfNecessaryMethod = AbstractAutoProxyCreator.class.getDeclaredMethod("wrapIfNecessary", Object.class, String.class, Object.class);
        wrapIfNecessaryMethod.setAccessible(true);

        // 该类中有方法要被增强
        Object t1 = wrapIfNecessaryMethod.invoke(creator, new Target1(), "target1", "target1");
        log.info("t1:{}", t1.getClass());

        // 该类中没有方法要被增强
        Object t2 = wrapIfNecessaryMethod.invoke(creator, new Target2(), "target2", "target2");
        log.info("t2:{}", t2.getClass());

        ((Target1) t1).foo();//代理类
        ((Target2) t2).bar();//原始类
    }

    static class Target1 {
        public void foo() {
            log.info("target1 foo()");
        }
    }

    static class Target2 {
        public void bar() {
            log.info("target2 bar()");
        }
    }

    /**
     * 高级切面类
     */
    @Aspect
    @Order(1)
    static class Aspect1 {
        @Before("execution(* foo())")
        public void before() {
            log.info("[Aspect]before...");
        }

        @After("execution(* foo())")
        public void after() {
            log.info("[Aspect]after...");
        }
    }

    @Configuration
    static class Config {

        @Bean
        public MethodInterceptor methodInterceptor() {
            return invocation -> {
                log.info("[Advisor]before...");
                Object proceed = invocation.proceed();
                log.info("[Advisor]after...");
                return proceed;
            };
        }

        /**
         * 低级切面
         */
        @Bean
        public Advisor advisor(MethodInterceptor methodInterceptor) {
            AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
            pointcut.setExpression("execution(* foo())");
            DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, methodInterceptor);
            // @Bean 上加 @Order 不生效
            advisor.setOrder(2);
            return advisor;
        }
    }
}

