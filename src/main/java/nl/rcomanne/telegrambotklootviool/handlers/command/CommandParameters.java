package nl.rcomanne.telegrambotklootviool.handlers.command;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommandParameters {
    private CommandType type;
    private long chatId;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> query;
}
