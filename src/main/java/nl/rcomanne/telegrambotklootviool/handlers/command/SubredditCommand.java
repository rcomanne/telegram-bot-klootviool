package nl.rcomanne.telegrambotklootviool.handlers.command;

import java.util.List;
import java.util.Random;

import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
class SubredditCommand extends Command {

    private final SubredditService service;
    private Random r;

    private SubredditCommand(final String botToken, final SubredditService service) {
        super(CommandType.SUBREDDIT, botToken);
        this.service = service;
        this.r = new Random();
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
        service.scrapeSubredditAsync(this.query);
        SubredditImage image = service.findRandomBySubreddit(this.query);
        if (image == null) {
            SendMessage message = new SendMessage()
                .setChatId(this.chatId)
                .setText("No images found for subreddit '" + this.query + "', trying to scrape now");
            send(message);
            List<SubredditImage> retrievedImages = service.scrapeAndSaveAllTime(this.query);
            sendItem(retrievedImages.get(r.nextInt(retrievedImages.size())));
        } else {
            sendItem(image);
            service.scrapeSubredditAsync(this.query);
        }
    }
}
