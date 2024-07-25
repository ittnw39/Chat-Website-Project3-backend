package com.elice.moduleuser;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class UserComponent {
    @Bean
    public void Hello()
    {
        System.out.println("Hello");
    }
}
