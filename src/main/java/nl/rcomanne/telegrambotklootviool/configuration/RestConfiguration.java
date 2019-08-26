package nl.rcomanne.telegrambotklootviool.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfiguration {
    private static final long CONNECT_TIMEOUT = 3;
    private static final long READ_TIMEOUT = 30;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(CONNECT_TIMEOUT))
                .setReadTimeout(Duration.ofSeconds(READ_TIMEOUT))
                .build();
    }
}
