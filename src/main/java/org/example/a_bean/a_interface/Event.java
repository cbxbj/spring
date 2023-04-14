package org.example.a_bean.a_interface;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.context.ApplicationContext;

@Data
@AllArgsConstructor
public class Event {
    private ApplicationContext applicationContext;
}
