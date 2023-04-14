package org.example.c_mvc.d_bindAndConvert;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.SimpleTypeConverter;

import java.util.Date;

/**
 * 仅有类型转换的功能
 */
@Slf4j
public class Test1SimpleConverter {
    public static void main(String[] args) {
        SimpleTypeConverter converter = new SimpleTypeConverter();
        Integer integer = converter.convertIfNecessary("13", int.class);
        log.info("int:{}", integer);
        Date date = converter.convertIfNecessary("1999/03/04", Date.class);
        log.info("date:{}", date);
    }
}
