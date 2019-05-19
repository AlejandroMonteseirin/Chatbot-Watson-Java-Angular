package es.us.chatbot.server.service;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.assistant.v1.Assistant;
import com.ibm.watson.assistant.v1.model.Context;
import com.ibm.watson.assistant.v1.model.MessageInput;
import com.ibm.watson.assistant.v1.model.MessageOptions;
import com.ibm.watson.assistant.v1.model.MessageResponse;
import es.us.chatbot.server.domain.Chat;
import es.us.chatbot.server.repository.ChatRepository;
import es.us.chatbot.server.service.dto.MessageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class TalkService {

    ChatRepository chatRepository;

    UserService userService;

    Assistant assistant;

    private static Map<Long, Context> contextCache = new HashMap<>(); // Guardo los contextos asi porque no da tiempo a hacerlo bien

    private static Map<String, String> amazonResults1 = new HashMap<>();
    private static Map<String, String> amazonResults2 = new HashMap<>();

    static {
        amazonResults1.put("Acer Predator PH315 Helios - 999€", "https://www.amazon.es/Acer-Predator-PH315-Helios-300/dp/B07G3DRLV5");
        amazonResults1.put("Lenovo Ideapad 330-15ICH - 649€", "https://www.amazon.es/Lenovo-Ideapad-330-15ICH-Ordenador-GTX1050-4GB/dp/B07JZZZSVG");
        amazonResults1.put("Acer Aspire A515-52G-73ML - 659€", "https://www.amazon.es/Acer-Aspire-A515-52G-73ML-Ordenador-port%C3%A1til/dp/B07MC3D2LB");

        amazonResults2.put("MSI GF72 8RD-081XES - 1049€", "https://www.amazon.es/MSI-GF72-8RD-081XES-Ordenador-operativo/dp/B07K5BDC17");
        amazonResults2.put("HP Laptop 15-da1016ns - 649€", "https://www.amazon.es/HP-Laptop-15-da1016ns-Ordenador-portátil/dp/B07M658QWV/");
        amazonResults2.put("ASUS K541UV-GQ650T - 740€", "https://www.amazon.es/ASUS-K541UV-GQ650T-Ordenador-Portátil-chocolate/dp/B01MTGBEWA");
    }

    public TalkService(ChatRepository chatRepository, UserService userService) {
        this.chatRepository = chatRepository;
        this.userService = userService;


        IamOptions options = new IamOptions.Builder()
            .apiKey("QHr9S5hFkVNU8LUVOunTh4jjIYimu7OqCTgh2vcyceud")
            .build();

        this.assistant = new Assistant("2019-02-28", options);
        this.assistant.setEndPoint("https://gateway-lon.watsonplatform.net/assistant/api");
    }

    public MessageDTO talk(MessageDTO messageDTO) {
        Chat chat;

        if (messageDTO.getChatId() == null || messageDTO.getChatId() == -1) {

            chat = new Chat()
                .context("sampleContext")
                .firstMessageDate(LocalDateTime.now());

        } else {

            chat = this.chatRepository.getOne(messageDTO.getChatId());

        }

        // Call Watson

        Context context = contextCache.getOrDefault(messageDTO.getChatId(), null);
        MessageInput input = new MessageInput();
        input.setText(messageDTO.getText());

        MessageOptions messageOptions = new MessageOptions.Builder()
            .workspaceId("0d854cf5-0adc-432c-8220-efd8b635223f")
            .input(input)
            .context(context)
            .build();

        MessageResponse response = assistant.message(messageOptions).execute().getResult();

        chat.setLastMessageDate(LocalDateTime.now());
        this.chatRepository.save(chat);

        contextCache.put(chat.getId(), response.getContext());

        List<Optional<Object>> opciones = new ArrayList<>();

        Optional<Object> cpu = Optional.ofNullable(response.getContext().get("cpu"));
        Optional<Object> ram = Optional.ofNullable(response.getContext().get("ram"));
        Optional<Object> gpu = Optional.ofNullable(response.getContext().get("gpu"));

        opciones.add(cpu);
        opciones.add(ram);
        opciones.add(gpu);

        String text = "";

        if (!opciones.stream().allMatch(Optional::isPresent) && opciones.stream().anyMatch(Optional::isPresent)){
            text += "Hasta ahora, he encontrado estos resultados: <br> " +
                amazonResults1.keySet().stream().map(k -> "<a href="+amazonResults1.get(k)+" target='_blank'>"+k+"</a>").collect(Collectors.joining("<br>"));

            text += "<br><br>";
        }

        text += response.getOutput().getText().stream().collect(Collectors.joining("<br>"));

        if (opciones.stream().allMatch(Optional::isPresent)){
            text += "<br><br>";

            text += "He encontrado estos resultados: <br> " +
                amazonResults2.keySet().stream().map(k -> "<a href="+amazonResults2.get(k)+" target='_blank'>"+k+"</a>").collect(Collectors.joining("<br>"));
        }

        return new MessageDTO(text, chat.getId());
    }
}
