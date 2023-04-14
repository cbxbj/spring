package org.example.c_mvc.d_bindAndConvert;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Date;

/**
 * 利用反射原理(set方式), 为 bean 的属性赋值
 */
@Slf4j
public class Test2BeanWrapper {
    public static void main(String[] args) {
        MyBean target = new MyBean();
        BeanWrapperImpl wrapper = new BeanWrapperImpl(target);
        wrapper.setPropertyValue("a", "10");
        wrapper.setPropertyValue("b", "hello");
        wrapper.setPropertyValue("c", "1999/03/04");
        log.info("target:{}", target);
    }

    @Data
    static class MyBean {
        private int a;
        private String b;
        private Date c;

    }
}
