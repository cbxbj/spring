package org.example.c_mvc.d_bindAndConvert;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.DirectFieldAccessor;

import java.util.Date;

/**
 * 利用反射原理(直接设置成员变量), 为 bean 的属性赋值
 */
@Slf4j
public class Test3FieldAccessor {
    public static void main(String[] args) {
        MyBean target = new MyBean();
        DirectFieldAccessor accessor = new DirectFieldAccessor(target);
        accessor.setPropertyValue("a", "10");
        accessor.setPropertyValue("b", "hello");
        accessor.setPropertyValue("c", "1999/03/04");
        log.info("target:{}", target);
    }

    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;

    }
}