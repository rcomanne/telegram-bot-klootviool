package nl.rcomanne.telegrambotklootviool.handlers.command;

import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;

@Slf4j
class SubredditCommand extends Command {

    private final SubredditService service;
    private List<String> bannedSubs;

    private SubredditCommand(final String botToken, final SubredditService service) {
        super(CommandType.SUBREDDIT, botToken);
        this.service = service;
    }

    SubredditCommand(final CommandParameters parameters, final String botToken, final SubredditService service, final List<String> bannedSubs) {
        this(botToken, service);
        this.chatId = parameters.getChatId();
        this.query = parameters.getQuery().orElse(null);
        this.bannedSubs = bannedSubs;
    }

    @Override
    void handle() {
        log.debug("handling subreddit command");
        sendItem(service.findRandom());
    }

    @Override
    void handleWithQuery() {
        log.debug("handling subreddit command for {}", this.query);
        if (bannedSubs.contains(this.query)) {
            sendMessage("Stop it, you filthy animal");
            return;
        }

        if (!service.subredditExistsAndHasItems(this.query)) {
            sendMessage(String.format("No images found for %s, trying to scrape now", this.query));
        }

        SubredditImage image = service.findRandomBySubreddit(this.query, String.valueOf(this.chatId));
        sendItem(image);
    }

    private void sendMessage(String text) {
        SendMessage message = new SendMessage()
            .setChatId(this.chatId)
            .setText(text);
        send(message);
    }
}
