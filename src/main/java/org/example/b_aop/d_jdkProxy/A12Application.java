package org.example.b_aop.d_jdkProxy;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用 ASM 动态生成字节码
 * 同一个方法/变量 通过反射获取第17次时，会生成一个代理类(一个方法/变量对应一个代理类)
 * 将反射变为直接调用,提高运行速度
 */
@Slf4j
public class A12Application {

    public static void main(String[] args) {
        Foo proxy = new $Proxy0((proxyObject, method, params) -> {
            //实现功能增强
            log.info("before");
            //调用目标
            return method.invoke(new Target(), params);
        });
        log.info("class:{}", proxy.getClass());
        proxy.foo();
        proxy.bar();
    }
}
