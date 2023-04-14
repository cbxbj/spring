package org.example.b_aop.j_around;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 责任链设计模式
 */
@Slf4j
public class ChainOfResponsibilityTemplate {

    static class Target {
        public void foo() {
            log.info("Target foo()");
        }
    }

    static class Advice1 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("Advice1 before");
            Object result = invocation.proceed();// 调用下一个通知或目标方法
            log.info("Advice1 after");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor {
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            log.info("Advice2 before");
            Object result = invocation.proceed();// 调用下一个通知或目标方法
            log.info("Advice2 after");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation {

        private final Object target;
        private final Method method;
        private final Object[] args;
        private final List<MethodInterceptor> methodInterceptorList;
        private int count = 1; // 调用次数

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return args;
        }

        /**
         * 调用每一个环绕通知 + 目标
         */
        @Override
        public Object proceed() throws Throwable {
            if (count > methodInterceptorList.size()) {
                //调用目标,返回并结束递归
                return method.invoke(target, args);
            }
            // 逐一调用
            MethodInterceptor methodInterceptor = methodInterceptorList.get(count++ - 1);
            return methodInterceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }

    @SneakyThrows
    public static void main(String[] args) {
        Target target = new Target();
        List<MethodInterceptor> list = List.of(new Advice1(), new Advice2());
        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], list);
        invocation.proceed();
    }
}
