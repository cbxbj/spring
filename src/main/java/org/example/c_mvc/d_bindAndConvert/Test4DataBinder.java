package org.example.c_mvc.d_bindAndConvert;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

import java.util.Date;

@Slf4j
public class Test4DataBinder {

    public static void main(String[] args) {
        // 执行数据绑定
        MyBean target = new MyBean();
        DataBinder dataBinder = new DataBinder(target);

        // 走成员变量
        dataBinder.initDirectFieldAccess();

        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("a", "10");
        pvs.add("b", "hello");
        pvs.add("c", "1999/03/04");
        dataBinder.bind(pvs);
        log.info("target:{}", target);
    }

    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}