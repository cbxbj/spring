package org.example.b_aop.e_cglibProxy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

@Slf4j
public class A13Application {
    @SuppressWarnings({"unused", "Convert2Lambda"})
    public static void main(String[] args) {
        Target target = new Target();
        Proxy proxy = new Proxy();
        proxy.setMethodInterceptor(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args1, MethodProxy proxy1) throws Throwable {
                log.info("before...");

                //return method.invoke(target, args1);  // 内部反射调用

                // FastClass
                return proxy1.invoke(target, args1);  // 内部 无反射,结合目标使用, spring 采用该方式
                //return proxy1.invokeSuper(obj, args1);  // 内部 无反射,结合代理使用
            }
        });

        proxy.save();
        proxy.save(1);
        proxy.save(2L);
    }
}
