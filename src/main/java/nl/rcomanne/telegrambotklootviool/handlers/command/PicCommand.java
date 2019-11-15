package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.service.GoogleSearchService;

import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PicCommand extends Command {

    private final GoogleSearchService search;

    PicCommand(CommandParameters parameters, String botToken, GoogleSearchService searchService) {
        this(botToken, searchService);
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery()
            .orElse(null);
    }

    private PicCommand(String botToken, GoogleSearchService searchService) {
        super(CommandType.PIC, botToken);
        this.search = searchService;
    }

    @Override
    void handle() {
        SendMessage sendMessage = new SendMessage().setChatId(chatId)
            .setText("Hello?! Please enter a search query!");

        send(sendMessage);
    }

    @Override
    void handleWithQuery() {
        String imgUrl;
        if (this.query.contains("nsfw")) {
            imgUrl = search.searchImageNSFW(this.query);
        } else {
            imgUrl = search.searchImageSFW(this.query);
        }
        log.info("sending photo using url: {}", imgUrl);

        if (imgUrl.contains(".gif")) {
            SendAnimation sendAnimation = new SendAnimation()
                .setChatId(this.chatId)
                .setAnimation(imgUrl);
            send(sendAnimation);
        } else {
            SendPhoto sendPhoto = new SendPhoto()
                .setChatId(this.chatId)
                .setPhoto(imgUrl);
            send(sendPhoto);
        }
    }
}
