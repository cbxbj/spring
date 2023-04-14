package org.example.c_mvc.c_parameterName;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.DefaultParameterNameDiscoverer;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 参数名获取
 * java --> class 后 参数名信息会消失
 * 解决办法:
 * a. javac -parameters xx.java
 * b. javac -g xx.java
 * 查看:
 * javap -c -v xx.class
 * a方式:参数名记录在 MethodParameters 中 能通过反射获取 接口上的参数名可以获取到
 * b方式:参数名记录在 LocalVariableTable 中 反射获取不到,通过 ASM 获取 接口上的参数名获取不到
 * <p>
 * {@link DefaultParameterNameDiscoverer}可解析2种方式的参数名
 */
@Slf4j
public class A22Application {
    @SneakyThrows
    public static void main(String[] args) {
        Method classMethod = Bean1.class.getMethod("foo", String.class, int.class);
        Method interfaceMethod = Bean2.class.getMethod("foo", String.class, int.class);
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        log.info("class:{}", Arrays.toString(discoverer.getParameterNames(classMethod)));
        log.info("class:{}", Arrays.toString(discoverer.getParameterNames(interfaceMethod)));
    }
}
