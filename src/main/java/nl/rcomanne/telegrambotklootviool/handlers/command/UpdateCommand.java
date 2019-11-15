package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateCommand extends Command {

    private final SubredditService service;

    private UpdateCommand(final String token, final SubredditService service) {
        super(CommandType.UPDATE, token);
        this.service = service;
    }

    UpdateCommand(final CommandParameters parameters, final String token, final SubredditService service) {
        this(token, service);
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery().orElse(null);
    }

    @Override
    void handle() {
        log.debug("handling update command");
        SendMessage message = new SendMessage()
            .setChatId(this.chatId)
            .setText("please select a subreddit to update");

        send(message);

    }

    @Override
    void handleWithQuery() {
        log.debug("handling update command for {}", this.query);
        service.scrapeSubredditAsync(this.query);
        sendItem("scraping subreddit async for: " + this.query, service.findRandom());
    }
}
