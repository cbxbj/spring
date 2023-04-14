package org.example.a_bean.h_scope;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MyController {
    @Lazy
    @Autowired
    private BeanForRequest beanForRequest;
    @Lazy
    @Autowired
    private BeanForSession beanForSession;
    @Lazy
    @Autowired
    private BeanForApplication beanForApplication;

    @GetMapping(value = "/test", produces = "text/html")
    public String test(HttpServletRequest request, HttpSession session) {
        ServletContext servletContext = request.getServletContext();
        String sb = "<ul>" +
                "<li>" + "request scope" + beanForRequest + "</li>" +
                "<li>" + "session scope" + beanForSession + "</li>" +
                "<li>" + "application scope" + beanForApplication + "</li>" +
                "</ul>";
        return sb;
    }

}
