package org.example.c_mvc.a_base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * {@link RequestMappingHandlerMapping}
 * {@link RequestMappingHandlerAdapter}
 */
@Slf4j
@SuppressWarnings("JavadocReference")
public class A20Application {
    public static void main(String[] args) {
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        handlerMappingAndAdapter(context);
    }

    /**
     * 1、{@link HandlerMapping} 解析 {@link RequestMapping} 及其派生注解
     * 生成路径与控制器方法的映射关系
     * 在初始化时就生成
     * 2、{@link DispatcherServlet#handlerAdapters} 根据 {@link HandlerMapping#getHandler(HttpServletRequest)} 获取对应的 {@link HandlerAdapter}
     * 3、{@link HandlerAdapter} 内部执行一些{@link HandlerMethodArgumentResolver} {@link HandlerMethodReturnValueHandler}等
     */
    @SneakyThrows
    @SuppressWarnings({"DataFlowIssue", "JavaReflectionInvocation"})
    private static void handlerMappingAndAdapter(ApplicationContext context) {
        RequestMappingHandlerMapping handlerMapping = context.getBean(RequestMappingHandlerMapping.class);

        // 获取映射结果
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
        handlerMethods.forEach((requestMappingInfo, handlerMethod) -> log.info("k:{}-->v:{}", requestMappingInfo, handlerMethod));

        // 模拟 request 请求
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/test4");
        // 设置参数
        request.addHeader("token", "myToken");
        request.setParameter("name", "张三");

        // 请求来了,获取控制器方法 返回处理器执行链对象
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        log.info("chain:{}", chain);

        // 执行处理
        RequestMappingHandlerAdapter handlerAdapter = context.getBean(RequestMappingHandlerAdapter.class);
        Method invokeHandlerMethod = RequestMappingHandlerAdapter.class.getDeclaredMethod("invokeHandlerMethod", HttpServletRequest.class, HttpServletResponse.class, HandlerMethod.class);
        invokeHandlerMethod.setAccessible(true);
        MockHttpServletResponse response = new MockHttpServletResponse();
        invokeHandlerMethod.invoke(handlerAdapter, request, response, chain.getHandler());

        // 获取所有的参数解析器
        for (HandlerMethodArgumentResolver argumentResolver : Objects.requireNonNull(handlerAdapter.getArgumentResolvers())) {
            log.info("argumentResolver:{}", argumentResolver);
        }

        // 获取所有的返回值处理器
        for (HandlerMethodReturnValueHandler returnValueHandler : Objects.requireNonNull(handlerAdapter.getReturnValueHandlers())) {
            log.info("returnValueHandler:{}", returnValueHandler);
        }

        byte[] content = response.getContentAsByteArray();
        log.info("content:{}", new String(content, StandardCharsets.UTF_8));
    }
}
