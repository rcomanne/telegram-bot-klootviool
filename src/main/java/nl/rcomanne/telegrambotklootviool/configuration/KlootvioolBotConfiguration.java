package nl.rcomanne.telegrambotklootviool.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;

import lombok.Getter;

@Configuration
public class KlootvioolBotConfiguration extends DefaultBotOptions {
    @Getter
    @Value("${bot.token}")
    private String botToken;

}
