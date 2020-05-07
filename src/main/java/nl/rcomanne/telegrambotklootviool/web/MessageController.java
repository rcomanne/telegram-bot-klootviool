package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.domain.SubredditImage;
import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.service.reddit.SubredditService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final SubredditService subredditService;
    private final MessageService messageService;

    @PostMapping("/{chatId}/{message}")
    public ResponseEntity<Void> sendMessage(@PathVariable("chatId") String chatId, @PathVariable("message") String message) {
        log.info("sending message");
        SubredditImage image = subredditService.findRandom();
        messageService.sendMessageWithPhoto(chatId, message, image);
        return ResponseEntity.ok().build();
    }
}
