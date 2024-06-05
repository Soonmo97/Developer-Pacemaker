package project.developer_pacemaker.dto.gpt;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
public class GptRequest {
    private String model;
    private List<Message> messages;

    public GptRequest(String model, String prompt) {
        this.model = model;
        this.messages =  new ArrayList<>();
        this.messages.add(new Message("user", prompt));
    }

}
