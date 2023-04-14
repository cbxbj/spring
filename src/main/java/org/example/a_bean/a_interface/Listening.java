package org.example.a_bean.a_interface;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Listening {

    @Async
    @EventListener
    public void listen(Event event) {
        log.info("event:{}", event);
    }
}
