package org.example.a_bean.b_implement;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationContextConfig {
    @Bean
    public Bean5 bean5() {
        return new Bean5();
    }

    @Bean
    public Bean6 bean6(Bean5 bean5) {
        Bean6 bean6 = new Bean6();
        bean6.setBean5(bean5);
        return bean6;
    }
}

class Bean5 {

}

@Data
class Bean6 {
    private Bean5 bean5;

}


