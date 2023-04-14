package org.example.c_mvc.a_base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yaml.snakeyaml.Yaml;

@Slf4j
@Controller
public class Controller1 {

    @GetMapping("/test1")
    public ModelAndView test1() {
        log.debug("test1()");
        return null;
    }

    @PostMapping("/test2")
    public ModelAndView test2(@RequestParam("name") String name) {
        log.debug("test2({})", name);
        return null;
    }

    @PutMapping("/test3")
    public ModelAndView test3(@Token String token) {
        log.debug("test3({})", token);
        return null;
    }

    @RequestMapping("/test4")
//    @ResponseBody
    @Yml
    public User test4() {
        log.debug("test4");
        return new User("张三", 18);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class User {
        private String name;
        private int age;
    }

    public static void main(String[] args) {
        String str = new Yaml().dump(new User("张三", 18));
        log.info("str:{}",str);
    }
}
