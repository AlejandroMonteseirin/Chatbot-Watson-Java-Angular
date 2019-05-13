package es.us.chatbot.server.service;

import es.us.chatbot.server.domain.Chat;
import es.us.chatbot.server.repository.ChatRepository;
import es.us.chatbot.server.service.dto.MessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
public class TalkService {

    @Autowired
    ChatRepository chatRepository;

    @Autowired
    UserService userService;

    //TODO: Call Watson
    public MessageDTO talk(MessageDTO messageDTO) {
        Chat chat;
        String msg;

        if (messageDTO.getChatId() == null || messageDTO.getChatId() == -1) {

            chat = new Chat()
                .context("sampleContext")
                .firstMessageDate(LocalDateTime.now());

            msg = "Hola, soy un chatbot y este es el primer mensaje de la conversación";

        } else {

            chat = this.chatRepository.getOne(messageDTO.getChatId());

            msg = "Seguimos hablando en la conversación";

        }

        // Call Watson

        chat.setLastMessageDate(LocalDateTime.now());
        this.chatRepository.save(chat);

        return new MessageDTO(msg + " con id=" + chat.getId(), chat.getId());
    }
}
