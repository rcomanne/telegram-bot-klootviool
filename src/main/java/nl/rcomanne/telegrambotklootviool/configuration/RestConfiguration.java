package nl.rcomanne.telegrambotklootviool.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class RestConfiguration {
    private static final long DEFAULT_TIMEOUT = 3;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .setReadTimeout(Duration.ofSeconds(DEFAULT_TIMEOUT))
                .build();
    }
}
