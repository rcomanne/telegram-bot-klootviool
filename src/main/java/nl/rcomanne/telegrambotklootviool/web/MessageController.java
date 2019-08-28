package nl.rcomanne.telegrambotklootviool.web;

import nl.rcomanne.telegrambotklootviool.service.MessageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService service;

    @PostMapping("/{chatId}/{message}")
    public ResponseEntity sendMessage(@PathVariable("chatId") String chatId, @PathVariable("message") String message) {
        log.info("sending message");
        service.sendMessageRandomPhoto(chatId, message);
        return ResponseEntity.ok().build();
    }
}
