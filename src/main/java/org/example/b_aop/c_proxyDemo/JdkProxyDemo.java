package org.example.b_aop.c_proxyDemo;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * jdk 动态代理
 * 目标类必须实现接口,目标类可以是final
 * 目标与代理类是兄弟关系，共同实现一个接口
 */
@Slf4j
public class JdkProxyDemo {

    interface Foo {
        void foo();
    }

    static final class Target implements Foo {

        @Override
        public void foo() {
            log.info("target foo()");
        }
    }

    @SneakyThrows
    @SuppressWarnings({"ResultOfMethodCallIgnored", "Convert2Lambda"})
    public static void main(String[] args) {
        Target target = new Target();
        Class<Target> targetClass = Target.class;
        Foo proxy = (Foo) Proxy.newProxyInstance(targetClass.getClassLoader(), targetClass.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxyObject, Method method, Object[] params) throws Throwable {
                log.info("before");
                // 执行方法
                Object result = method.invoke(target, params);
                log.info("after");
                return result;
            }
        });
        proxy.foo();
        log.info("class:{}", proxy.getClass());
        System.in.read();
    }
}
