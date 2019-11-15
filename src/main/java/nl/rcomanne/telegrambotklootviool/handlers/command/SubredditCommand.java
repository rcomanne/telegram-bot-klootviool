package nl.rcomanne.telegrambotklootviool.handlers.command;

import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SubredditCommand extends Command {

    private final SubredditService service;

    private SubredditCommand(final String botToken, final SubredditService service) {
        super(CommandType.SUBREDDIT, botToken);
        this.service = service;
    }

    SubredditCommand(final CommandParameters parameters, final String botToken, final SubredditService service) {
        this(botToken, service);
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery().orElse(null);
    }

    @Override
    void handle() {
        log.debug("handling subreddit command");
        sendItem(service.findRandom());
    }

    @Override
    void handleWithQuery() {
        log.debug("handling subreddit command for {}", this.query);
        sendItem(service.findRandomBySubreddit(this.query));
        sendItem("scraping subreddit " + this.query, service.findRandom());
        service.scrapeSubredditAsync(this.query);
    }
}
