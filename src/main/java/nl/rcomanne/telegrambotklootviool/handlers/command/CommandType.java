package nl.rcomanne.telegrambotklootviool.handlers.command;

public enum CommandType {
    PIC,
    SUBREDDIT,
    UPDATE,
    CHAT_ID;

    public static CommandType commandTypeOf(String name) {
        if (name.equalsIgnoreCase("pic")) {
            return PIC;
        }
        if (name.equalsIgnoreCase("r") || name.equalsIgnoreCase("subreddit")) {
            return SUBREDDIT;
        }
        if (name.equalsIgnoreCase("update")) {
            return UPDATE;
        }
        if (name.equalsIgnoreCase("chatId")) {
            return CHAT_ID;
        }
        throw new IllegalArgumentException("unknown type " + name);
    }
}
