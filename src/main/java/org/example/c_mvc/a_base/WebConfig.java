package org.example.c_mvc.a_base;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.List;

@Configuration
@ComponentScan
@PropertySource("classpath:application.properties")
@EnableConfigurationProperties({WebMvcProperties.class, ServerProperties.class})
public class WebConfig {
    /**
     * 内嵌Web容器的工厂
     */
    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory(ServerProperties serverProperties) {
        return new TomcatServletWebServerFactory(serverProperties.getPort());
    }

    /**
     * {@link DispatcherServlet}
     * 默认:初始化是tomcat容器首次使用到 DispatcherServlet 时,tomcat会初始化 DispatcherServlet
     */
    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    /**
     * 将{@link DispatcherServlet} 注册到 tomcat容器中
     */
    @Bean
    public DispatcherServletRegistrationBean dispatcherServletRegistrationBean(DispatcherServlet dispatcherServlet, WebMvcProperties webMvcProperties) {
        DispatcherServletRegistrationBean bean = new DispatcherServletRegistrationBean(dispatcherServlet, "/");
        // 大于 0 则会在 tomcat 启动时 初始化 Servlet ,多个 Servlet 时,数字越小优先级越高
        bean.setLoadOnStartup(webMvcProperties.getServlet().getLoadOnStartup());
        return bean;
    }

    /**
     * 如果用{@link DispatcherServlet}初始化时默认添加的组件,并不会作为 bean
     * 直接加入则会作为 bean
     */
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    /**
     * 如果用{@link DispatcherServlet}初始化时默认添加的组件,并不会作为 bean
     * 直接加入则会作为 bean
     */
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapter();
        // 添加自定义参数解析器
        adapter.setArgumentResolvers(List.of(new TokenArgumentResolver()));
        adapter.setReturnValueHandlers(List.of(new YamlReturnValueHandler()));
        return adapter;
    }

}
