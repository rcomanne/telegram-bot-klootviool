package nl.rcomanne.telegrambotklootviool.scheduled;

import nl.rcomanne.telegrambotklootviool.service.MessageService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private static final String CHAT_ID = "-320932775";

    private final MessageService messageService;

    @Scheduled(cron = "* 0/15 * * * *")
    public void sendDailyMessage() {
        messageService.sendMessage(CHAT_ID, "goedemorgen thirsty bois");
    }
}
