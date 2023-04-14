package org.example.b_aop.i_annotationAwareAspectJAutoProxyCreator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 高级切面 --> 低级切面
 */
@Slf4j
public class A17_2Application {

    static class Aspect {
        @Before("execution(* foo())")
        public void before1() {
            log.info("before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            log.info("before2");
        }

        @After("execution(* foo())")
        public void after() {
            log.info("after");
        }

        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            log.info("afterReturning");
        }

        @AfterThrowing("execution(* foo())")
        public void AfterThrowing() {
            log.info("AfterThrowing");
        }

        @SneakyThrows
        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint point) {
            log.info("around before...");
            Object result = point.proceed();
            log.info("around after...");
            return result;
        }
    }

    @SuppressWarnings("unused")
    static class Target {
        public void foo() {
            log.info("foo()");
        }
    }

    @SuppressWarnings("DuplicatedCode")
    public static void main(String[] args) {
        AspectInstanceFactory factory = new SingletonAspectInstanceFactory(new Aspect());
        List<Advisor> list = new ArrayList<>();
        for (Method method : Aspect.class.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Before.class)) {
                // 切点
                String expression = method.getAnnotation(Before.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJMethodBeforeAdvice advice = new AspectJMethodBeforeAdvice(method, pointcut, factory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        for (Advisor advisor : list) {
            log.info("advisor:{}", advisor);
        }
    }

}
