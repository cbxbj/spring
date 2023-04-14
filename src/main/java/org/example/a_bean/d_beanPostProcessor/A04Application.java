package org.example.a_bean.d_beanPostProcessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/**
 * Bean 后处理器
 */
@Slf4j
public class A04Application {
    /**
     * {@link GenericApplicationContext} 是一个【干净】(未添加任何后处理器)的容器
     * {@link GenericApplicationContext#refresh()}添加并执行 bean 后处理器、beanFactory 后处理器 并 初始化所有的单例
     */
    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);

        parseAutoWired(context);

        paresResource(context);

        paresConfigurationProperties(context);

        // 初始化容器
        context.refresh();
        log.info("Bean4:{}", context.getBean(Bean4.class));
        context.close();
    }

    /**
     * 解析
     * {@link Autowired} 依赖注入
     * {@link Value} 依赖注入
     */
    private static void parseAutoWired(GenericApplicationContext context) {
        // 解析 @Value 中值的内容
        context.getDefaultListableBeanFactory()
                .setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);
    }

    /**
     * 解析
     * {@link jakarta.annotation.Resource} 依赖注入
     * {@link jakarta.annotation.PostConstruct} 初始化前
     * {@link jakarta.annotation.PreDestroy} 销毁
     */
    private static void paresResource(GenericApplicationContext context) {
        context.registerBean(CommonAnnotationBeanPostProcessor.class);
    }

    /**
     * 解析{@link ConfigurationProperties} 初始化前
     */
    private static void paresConfigurationProperties(GenericApplicationContext context) {
        context.registerBean("bean4", Bean4.class);
        ConfigurationPropertiesBindingPostProcessor.register(context);
    }
}
