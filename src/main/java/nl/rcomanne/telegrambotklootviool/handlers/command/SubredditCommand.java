package nl.rcomanne.telegrambotklootviool.handlers.command;

import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Random;

@Slf4j
class SubredditCommand extends Command {

    private final SubredditService service;
    private Random r;
    private List<String> bannedSubs;

    private SubredditCommand(final String botToken, final SubredditService service) {
        super(CommandType.SUBREDDIT, botToken);
        this.service = service;
        this.r = new Random();
        this.bannedSubs = List.of("bbw", "chubbygonewild");
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
        for (String banned : bannedSubs) {
            if (banned.equalsIgnoreCase(this.query)) {
                sendMessage("Stop it, you filthy animal");
                return;
            }
        }

        service.scrapeSubredditAsync(this.query);
        SubredditImage image = service.findRandomBySubreddit(this.query);
        if (image == null) {
            sendMessage("No images found for subreddit '" + this.query + "', trying to scrape now");
            List<SubredditImage> retrievedImages = service.scrapeAndSaveAllTime(this.query);
            if (retrievedImages.isEmpty()) {
                sendMessage("No images found for subreddit '" + this.query + "'");
            } else {
                sendItem(retrievedImages.get(r.nextInt(retrievedImages.size())));
            }
        } else {
            sendItem(image);
            service.scrapeSubredditAsync(this.query);
        }
    }

    private void sendMessage(String text) {
        SendMessage message = new SendMessage()
            .setChatId(this.chatId)
            .setText(text);
        send(message);
    }
}
