package com.botbye.springdemo.configuration;

import com.botbye.Botbye;
import com.botbye.model.BotbyeConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public Botbye botbye() {
        BotbyeConfig config = new BotbyeConfig.Builder()
                .serverKey("00000000-0000-0000-0000-000000000000") // Use your project server-key
                .build();

        return new Botbye(config);
    }
}
