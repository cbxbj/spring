package org.example.b_aop.g_switchProxy;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * 两个切面:
 * aspect、advisor
 * aspect: 由 通知(advice)、切点(pointcut) 组成,可写多个通知+切点
 * advisor: 更系粒度的切面,包含一个通知和一个切点
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class A15Application {
    /**
     * 演示低级切面
     */
    public static void main(String[] args) {
        // 1、备好切点
        AspectJExpressionPointcut pointcut = getPointcut();
        // 2、备好通知
        MethodInterceptor advice = getAdvice();
        // 3、准备切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        // 4、创建代理
        createProxy(advisor);
    }

    /**
     * 创建代理
     * a.{@link ProxyConfig#proxyTargetClass} = false 且 目标实现了接口 用 jdk 代理实现
     * b.{@link ProxyConfig#proxyTargetClass} = false 且 目标没有实现了接口 用 cglib 代理实现
     * c.{@link ProxyConfig#proxyTargetClass} = true 用 cglib 代理实现
     */
    private static void createProxy(DefaultPointcutAdvisor advisor) {
        Target target = new Target();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvisor(advisor);
        // 设置接口 否则 工厂 无法知道 目标类 是否实现了接口,默认未实现接口
        factory.setInterfaces(target.getClass().getInterfaces());
        // 为 true 必用 cglib 实现
        factory.setProxyTargetClass(true);
        I proxy = (I) factory.getProxy();
        log.info("proxy class:{}", proxy.getClass());
        proxy.foo();
        proxy.bar();
    }

    private static MethodInterceptor getAdvice() {
        return invocation -> {
            log.info("before...");
            Object result = invocation.proceed(); //调用目标
            log.info("after...");
            return result;
        };
    }

    private static AspectJExpressionPointcut getPointcut() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        return pointcut;
    }

    interface I {

        void foo();

        void bar();
    }

    static class Target implements I {

        @Override
        public void foo() {
            log.info("target1 foo()");
        }

        @Override
        public void bar() {
            log.info("target1 bar()");
        }
    }

}
