package nl.rcomanne.telegrambotklootviool.handlers.command;


import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.instagram.InstagramService;

@Slf4j
public class InstagramCommand extends Command {

    private final InstagramService service;

    InstagramCommand(final CommandParameters parameters, final String botToken, final InstagramService service) {
        super(CommandType.INSTA, botToken);
        this.service = service;
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery().orElse(null);
    }

    @Override
    void handle() {
        log.debug("handling Instagram command");
        sendItem(service.getRandomImage());
    }

    @Override
    void handleWithQuery() {
        log.debug("handling Instagram command for {}", this.query);
        sendItem(service.getRandomImageFromAccount(this.query, this.chatId));
    }
}
