package org.example.c_mvc.e_controllerAdvice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * {@link ControllerAdvice}控制器的通知,增强控制器的功能
 * 1、{@link ExceptionHandler} 处理异常
 * 2、{@link InitBinder} 添加自定义类型转换器 参数必然是:{@link WebDataBinder}
 * 3、{@link ModelAttribute} 返回值 补充模型数据
 * <p>
 * {@link InitBinder} 加在 {@link ControllerAdvice} 和 {@link Controller} 区别:
 * {@link ControllerAdvice} 全局生效(所有{@link Controller}都生效)
 * {@link Controller} 局部生效
 */
@Controller
public class WebConfig {

    @ControllerAdvice
    static class MyControllerAdvice {
        @InitBinder
        public void binder3(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder3 转换器"));
        }
    }

    @Controller
    static class Controller1 {
        @InitBinder
        public void binder1(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder1 转换器"));
        }

        public void foo() {

        }
    }

    @Controller
    static class Controller2 {
        @InitBinder
        public void binder21(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder21 转换器"));
        }

        @InitBinder
        public void binder22(WebDataBinder webDataBinder) {
            webDataBinder.addCustomFormatter(new MyDateFormatter("binder22 转换器"));
        }

        public void bar() {

        }
    }
}
