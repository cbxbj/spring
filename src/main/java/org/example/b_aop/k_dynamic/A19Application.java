package org.example.b_aop.k_dynamic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.framework.autoproxy.AbstractAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.support.GenericApplicationContext;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class A19Application {

    @Aspect
    static class MyAspect {
        /**
         * 静态通知调用,不带参数绑定
         */
        @Before("execution(* foo(..))")
        public void before1() {
            log.info("before1");
        }

        /**
         * 动态通知调用,需要参数绑定
         */
        @Before("execution(* foo(..)) && args(x)")
        public void before2(int x) {
            log.info("before2:{}", x);
        }

    }

    static class Target {
        public void foo(int x) {
            log.info("target foo():{}", x);
        }
    }


    @Configuration
    static class MyConfig {
        /**
         * 高级切面转为低级切面
         * 创建代理对象
         */
        @Bean
        AnnotationAwareAspectJAutoProxyCreator proxyCreator() {
            return new AnnotationAwareAspectJAutoProxyCreator();
        }

        @Bean
        public MyAspect myAspect() {
            return new MyAspect();
        }
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean(ConfigurationClassPostProcessor.class);
        context.registerBean(MyConfig.class);
        context.refresh();

        AnnotationAwareAspectJAutoProxyCreator creator = context.getBean(AnnotationAwareAspectJAutoProxyCreator.class);
        Method findEligibleAdvisorsMethod = AbstractAdvisorAutoProxyCreator.class.getDeclaredMethod("findEligibleAdvisors", Class.class, String.class);
        findEligibleAdvisorsMethod.setAccessible(true);
        List<Advisor> list = (List<Advisor>) findEligibleAdvisorsMethod.invoke(creator, Target.class, "target");
        Target target = new Target();
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setTarget(target);
        proxyFactory.addAdvisors(list);
        Object proxy = proxyFactory.getProxy();

        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo", int.class), Target.class);
        for (Object o : methodInterceptorList) {
            log.info("o:{}", o);
        }

        Constructor<ReflectiveMethodInvocation> constructor = ReflectiveMethodInvocation.class.getDeclaredConstructor(Object.class, Object.class, Method.class, Object[].class, Class.class, List.class);
        constructor.setAccessible(true);
        MethodInvocation methodInvocation = constructor.newInstance(proxy, new Target(), Target.class.getMethod("foo", int.class), new Object[]{100}, Target.class, methodInterceptorList);
        methodInvocation.proceed();
    }

}
