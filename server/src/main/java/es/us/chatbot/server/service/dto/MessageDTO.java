package es.us.chatbot.server.service.dto;

import java.time.LocalDate;

public class MessageDTO {

    private String text;
    private Long chatId;

    public MessageDTO() {

    }

    public MessageDTO(String text, Long chatId) {
        this.text = text;
        this.chatId = chatId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "text='" + text + '\'' +
            ", chatId=" + chatId +
            '}';
    }
}
