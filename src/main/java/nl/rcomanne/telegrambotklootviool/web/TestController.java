package nl.rcomanne.telegrambotklootviool.web;

import nl.rcomanne.telegrambotklootviool.service.MessageService;
import nl.rcomanne.telegrambotklootviool.web.dto.TestDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendAnimation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MessageService service;

    @GetMapping("/{link}")
    public void testAnimation(@PathVariable("link") String link) {
        SendAnimation animation = new SendAnimation()
            .setAnimation(link)
            .setCaption("Testing with link: " + link)
            .setChatId("620393195");
        service.sendAnimation(animation);
    }

    @PostMapping
    public void testAnimation(@RequestBody TestDto dto) {
        SendAnimation animation = new SendAnimation()
            .setAnimation(dto.getUrl())
            .setCaption("Testing with link: " + dto.getUrl())
            .setChatId("620393195");
        service.sendAnimation(animation);
    }

}
