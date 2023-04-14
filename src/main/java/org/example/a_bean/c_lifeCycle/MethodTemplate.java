package org.example.a_bean.c_lifeCycle;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 模版方法设计模式
 */
@Slf4j
public class MethodTemplate {

    public static void main(String[] args) {
        MyBeanFactory beanFactory = new MyBeanFactory();
        BeanPostProcessor autowired = bean -> log.info("@Autowired:{}", bean);
        BeanPostProcessor resource = bean -> log.info("@Resource:{}", bean);
        beanFactory.addBeanPostProcessor(autowired, resource);
        log.info("{}", beanFactory.getBean());
    }

    static class MyBeanFactory {

        private final List<BeanPostProcessor> processors = new ArrayList<>();

        public Object getBean() {
            Object bean = new Object();
            log.info("构造:{}", bean);
            processors.forEach(beanPostProcessor -> beanPostProcessor.inject(bean));
            log.info("依赖注入:{}", bean);
            log.info("初始化:{}", bean);
            return bean;
        }

        public void addBeanPostProcessor(BeanPostProcessor... beanPostProcessor) {
            processors.addAll(Arrays.asList(beanPostProcessor));
        }
    }

    interface BeanPostProcessor {
        void inject(Object bean); // 对依赖注入阶段的扩展
    }

}
