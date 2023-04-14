package org.example.c_mvc.d_bindAndConvert;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;

import java.util.Date;

@Slf4j
public class Test5ServletDataBinder {
    public static void main(String[] args) {
        // 执行数据绑定
        MyBean target = new MyBean();

        // ServletModelAttributeMethodProcessor 内部使用该方式
        DataBinder dataBinder = new ServletRequestDataBinder(target);

        // 走成员变量
        dataBinder.initDirectFieldAccess();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("a", "10");
        request.setParameter("b", "hello");
        request.setParameter("c", "1999/03/04");

        dataBinder.bind(new ServletRequestParameterPropertyValues(request));

        log.info("target:{}", target);
    }

    @ToString
    static class MyBean {
        private int a;
        private String b;
        private Date c;
    }
}
