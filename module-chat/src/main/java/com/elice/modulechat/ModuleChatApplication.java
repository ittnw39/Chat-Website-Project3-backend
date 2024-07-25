package com.elice.modulechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// load multimodule
@ComponentScan(value = {"com.elice.modulefile", "com.elice.common"})
@SpringBootApplication
public class ModuleChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleChatApplication.class, args);
    }

}
