package org.example.b_aop.d_jdkProxy;

import java.lang.reflect.Method;

/**
 * 模拟 {@link java.lang.reflect.InvocationHandler}
 */
@SuppressWarnings("unused")
public interface InvocationHandler {
    Object invoke(Object proxyObject, Method method, Object[] params) throws Throwable;
}