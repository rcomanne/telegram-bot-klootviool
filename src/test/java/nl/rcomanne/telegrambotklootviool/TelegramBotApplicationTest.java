package nl.rcomanne.telegrambotklootviool;

import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.meta.generics.WebhookBot;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TelegramBotApplicationTest {

    @Test
    public void contextLoads() {
        System.out.println("yup");
    }


    @Configuration
    static class MockTelegramBotsApi{

        @Bean
        public TelegramBotsApi telegramBotsApi() {
            return mock(TelegramBotsApi.class);
        }
    }

    @Configuration
    static class LongPollingBotConfig{
        @Bean
        public LongPollingBot longPollingBot() {
            return mock(LongPollingBot.class);
        }
    }

    @Configuration
    static class WebhookBotConfig{
        @Bean
        public WebhookBot webhookBot() {
            return mock(WebhookBot.class);
        }
    }
}
