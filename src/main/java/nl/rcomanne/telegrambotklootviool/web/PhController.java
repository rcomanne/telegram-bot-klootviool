package nl.rcomanne.telegrambotklootviool.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rcomanne.telegrambotklootviool.service.ProgrammerHumorService;
import nl.rcomanne.telegrambotklootviool.web.dto.PhDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ph")
@RequiredArgsConstructor
public class PhController {

    private final ProgrammerHumorService service;

    @PostMapping
    public ResponseEntity<String> postItem(@RequestBody PhDto dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PostMapping("/list")
    public ResponseEntity<List<String>> postItems(@RequestBody List<PhDto> dtos) {
        return ResponseEntity.ok(service.save(dtos));
    }
}
