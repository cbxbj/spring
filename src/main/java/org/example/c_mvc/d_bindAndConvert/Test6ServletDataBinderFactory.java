package org.example.c_mvc.d_bindAndConvert;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.bind.ServletRequestParameterPropertyValues;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestDataBinderFactory;

import java.util.Date;

@Slf4j
public class Test6ServletDataBinderFactory {
    @SneakyThrows
    public static void main(String[] args) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 绑定失败,需自定义转换器
        // 2、用 @InitBinder 转换(jdk方式) PropertyEditorRegistry PropertyEditor
        // 3、用 ConversionService 转换(spring方式) ConversionService Formatter
        request.setParameter("birthday", "1999|01|02");
        request.setParameter("address.name", "南京");


        User target = new User();
        // 1、用工厂 无转换功能

        ServletRequestDataBinderFactory factory = new ServletRequestDataBinderFactory(null, null);
        WebDataBinder dataBinder = factory.createBinder(new ServletWebRequest(request), target, "user");
        dataBinder.bind(new ServletRequestParameterPropertyValues(request));
        log.info("target:{}", target);
    }


    @Data
    static class User {
        private Date birthday;
        private Address address;
    }

    @Data
    static class Address {
        private String name;
    }
}
