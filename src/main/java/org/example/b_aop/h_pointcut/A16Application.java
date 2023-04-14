package org.example.b_aop.h_pointcut;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * 切点匹配
 * {@link MergedAnnotations}
 */
@Slf4j
public class A16Application {
    @SneakyThrows
    public static void main(String[] args) {
        // 解析 切点表达式
        parsePointcut();

        // 解析 @Transactional 的方式
        parseTransactional();
    }

    private static void parsePointcut() throws NoSuchMethodException {
        AspectJExpressionPointcut pointcut1 = new AspectJExpressionPointcut();
        pointcut1.setExpression("execution(* bar())");

        log.info("foo方法 是否与切点表达式匹配:{}", pointcut1.matches(T1.class.getMethod("foo"), T1.class));
        log.info("bar方法 是否与切点表达式匹配:{}", pointcut1.matches(T1.class.getMethod("bar"), T1.class));

        AspectJExpressionPointcut pointcut2 = new AspectJExpressionPointcut();
        pointcut2.setExpression("@annotation(org.springframework.transaction.annotation.Transactional)");
        log.info("foo方法 是否与切点表达式匹配:{}", pointcut2.matches(T1.class.getMethod("foo"), T1.class));
        log.info("bar方法 是否与切点表达式匹配:{}", pointcut2.matches(T1.class.getMethod("bar"), T1.class));
    }

    /**
     * 解析{@link Transactional}注解
     * 并非使用{@link AspectJExpressionPointcut}
     * 原因:该注解可加在方法上、类上、接口上,切点表达式只能匹配方法信息
     */
    @SneakyThrows
    private static void parseTransactional() {
        StaticMethodMatcherPointcut pointcut = new StaticMethodMatcherPointcut() {
            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                // 查看方法上是否加了 @Transactional
                MergedAnnotations annotations = MergedAnnotations.from(method);
                if (annotations.isPresent(Transactional.class)) {
                    return true;
                }

                // 查看类上(本类、父类、接口)是否加了 @Transactional
                annotations = MergedAnnotations.from(targetClass, MergedAnnotations.SearchStrategy.TYPE_HIERARCHY);
                return annotations.isPresent(Transactional.class);
            }
        };
        log.info("T1 foo方法 是否与切点表达式匹配:{}", pointcut.matches(T1.class.getMethod("foo"), T1.class));
        log.info("T2 foo方法 是否与切点表达式匹配:{}", pointcut.matches(T2.class.getMethod("foo"), T2.class));
        log.info("T3 foo方法 是否与切点表达式匹配:{}", pointcut.matches(T3.class.getMethod("foo"), T3.class));

    }

    static class T1 {
        @Transactional
        public void foo() {

        }

        public void bar() {

        }
    }

    @Transactional
    static class T2 {
        public void foo() {

        }
    }

    @Transactional
    interface I1 {
        void foo();
    }

    static class T3 implements I1 {

        @Override
        public void foo() {

        }
    }
}
