package org.example.b_aop.j_around;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.*;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.framework.ReflectiveMethodInvocation;
import org.springframework.aop.framework.adapter.AfterReturningAdviceInterceptor;
import org.springframework.aop.framework.adapter.MethodBeforeAdviceInterceptor;
import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 所有的通知最后都要转为环绕通知 {@link MethodInterceptor}
 * 原因:
 * a. 因为 advisor 有多个, 且一个套一个调用, 因此需要一个调用链对象, 即 {@link MethodInterceptor}
 * b. {@link MethodInterceptor} 要知道 advice 有哪些, 还要知道目标, 调用次序如下
 * 将 {@link MethodInvocation} 放入当前线程
 * |-> before1 ----------------------------------- 从当前线程获取 {@link MethodInvocation}
 * |                                             |
 * |   |-> before2 --------------------          | 从当前线程获取 {@link MethodInvocation}
 * |   |                              |          |
 * |   |   |-> target ------ 目标   advice2    advice1
 * |   |                              |          |
 * |   |-> after2 ---------------------          |
 * |                                             |
 * |-> after1 ------------------------------------
 * c. 从上图看出, 环绕通知才适合作为 advice, 因此其他 {@link Before}、{@link AfterReturning} 都会被转换成环绕通知
 * d. 统一转换为环绕通知, 体现的是设计模式中的适配器模式
 * - 对外是为了方便使用要区分 {@link Before}、{@link AfterReturning}
 * - 对内统一都是环绕通知, 统一用 {@link MethodInterceptor} 表示
 * <p>
 * 此步获取所有执行时需要的 advice (静态)
 * a. 即统一转换为 @link MethodInterceptor} 环绕通知, 这体现在方法名中的 Interceptors 上
 * b. 适配如下 工厂 + 策略 + 适配器
 * - {@link org.springframework.aop.framework.adapter.MethodBeforeAdviceAdapter} 将 {@link Before} AspectJMethodBeforeAdvice 适配为 {@link MethodBeforeAdviceInterceptor}
 * - {@link org.springframework.aop.framework.adapter.AfterReturningAdviceAdapter} 将 {@link AfterReturning} AspectJAfterReturningAdvice 适配为 {@link AfterReturningAdviceInterceptor}
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class A18Application {

    static class Aspect {
        /**
         * {@link AspectJMethodBeforeAdvice}
         */
        @Before("execution(* foo())")
        public void before1() {
            log.info("before1");
        }

        @Before("execution(* foo())")
        public void before2() {
            log.info("before2");
        }

        /**
         * 环绕通知:{@link AspectJAfterAdvice}
         */
        @After("execution(* foo())")
        public void after() {
            log.info("after");
        }

        /**
         * {@link AspectJAfterReturningAdvice}
         */
        @AfterReturning("execution(* foo())")
        public void afterReturning() {
            log.info("afterReturning");
        }

        /**
         * 环绕通知:{@link AspectJAfterThrowingAdvice}
         */
        @AfterThrowing("execution(* foo())")
        public void AfterThrowing() {
            log.info("AfterThrowing");
        }

        /**
         * 环绕通知:{@link AspectJAroundAdvice}
         */
        @SneakyThrows
        @Around("execution(* foo())")
        public Object around(ProceedingJoinPoint point) {
            log.info("around before...");
            Object result = point.proceed();
            log.info("around after...");
            return result;
        }
    }

    static class Target {
        public void foo() {
            log.info("foo()");
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        List<Advisor> advisorList = getAdvisor();

        List<Object> methodInterceptorList = convert(advisorList);

        // 最外层加一个环绕通知,准备把 MethodInvocation 放到当前线程
        methodInterceptorList.add(0, ExposeInvocationInterceptor.INSTANCE);

        // 创建并执行调用链:环绕通知 + 目标
        Constructor<ReflectiveMethodInvocation> constructor = ReflectiveMethodInvocation.class.getDeclaredConstructor(Object.class, Object.class, Method.class, Object[].class, Class.class, List.class);
        constructor.setAccessible(true);
        MethodInvocation methodInvocation = constructor.newInstance(null, new Target(), Target.class.getMethod("foo"), new Object[0], Target.class, methodInterceptorList);
        // 执行通知 + 目标方法
        methodInvocation.proceed();

    }

    /**
     * 将非环绕通知 转为 环绕通知
     */
    @SneakyThrows
    private static List<Object> convert(List<Advisor> advisorList) {
        ProxyFactory proxyFactory = new ProxyFactory();
        // 设置目标
        proxyFactory.setTarget(new Target());
        // 添加切面
        proxyFactory.addAdvisors(advisorList);

        // 将非环绕通知 转为 环绕通知
        List<Object> methodInterceptorList = proxyFactory.getInterceptorsAndDynamicInterceptionAdvice(Target.class.getMethod("foo"), Target.class);
        for (Object methodInterceptor : methodInterceptorList) {
            log.info("methodInterceptor:{}", methodInterceptor);
        }
        return methodInterceptorList;
    }

    /**
     * 高级切面 -> 低级切面
     */
    @SuppressWarnings("DuplicatedCode")
    private static List<Advisor> getAdvisor() {
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
            } else if (method.isAnnotationPresent(AfterReturning.class)) {
                // 切点
                String expression = method.getAnnotation(AfterReturning.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJAfterReturningAdvice advice = new AspectJAfterReturningAdvice(method, pointcut, factory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            } else if (method.isAnnotationPresent(Around.class)) {
                // 切点
                String expression = method.getAnnotation(Around.class).value();
                AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
                pointcut.setExpression(expression);

                // 通知类
                AspectJAroundAdvice advice = new AspectJAroundAdvice(method, pointcut, factory);

                // 切面
                Advisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
                list.add(advisor);
            }
        }
        for (Advisor advisor : list) {
            log.info("advisor:{}", advisor);
        }
        return list;
    }

}
