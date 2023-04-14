package org.example.b_aop.c_proxyDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib 动态代理
 * 目标类、方法必须不能被 final 修饰
 * 目标类与代理类是父子关系
 */
@Slf4j
public class CglibProxyDemo {
    static class Target {
        public void foo() {
            log.info("target foo()");
        }
    }

    @SuppressWarnings({"CommentedOutCode", "unused", "Convert2Lambda"})
    public static void main(String[] args) {
        Target target = new Target();
        // 1、代理类 2、方法 3、方法参数 4、方法
        Target proxy = (Target) Enhancer.create(Target.class, new MethodInterceptor() {
            @Override
            public Object intercept(Object proxyObject, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
                log.info("before");

                //Object invoke = method.invoke(target, params); // 使用反射调用

                //Object invoke = methodProxy.invoke(target, params); // 避免反射调用,需要目标类,spring使用该方式

                Object invoke = methodProxy.invokeSuper(proxyObject, params);// 避免反射调用,需要代理类
                log.info("after");
                return invoke;
            }
        });
        proxy.foo();
    }
}
