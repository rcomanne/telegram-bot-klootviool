package nl.rcomanne.telegrambotklootviool.web;

import java.util.List;

import nl.rcomanne.telegrambotklootviool.service.NsfwService;
import nl.rcomanne.telegrambotklootviool.web.dto.NsfwDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/nsfw")
@RequiredArgsConstructor
public class NsfwController {

    private final NsfwService service;

    @PostMapping
    public ResponseEntity<String> postItem(@RequestBody NsfwDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PostMapping("/list")
    public ResponseEntity<List<String>> postItems(@RequestBody List<NsfwDto> dtos) {
        return ResponseEntity.ok(service.save(dtos));
    }
}
