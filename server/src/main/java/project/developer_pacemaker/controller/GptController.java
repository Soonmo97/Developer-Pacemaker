package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.developer_pacemaker.dto.gpt.GptRequest;
import project.developer_pacemaker.dto.gpt.GptResponse;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/ask")
    public String chat(@RequestParam(name = "prompt")String prompt){
        GptRequest request = new GptRequest(model, prompt);
        GptResponse chatGPTResponse =  template.postForObject(apiURL, request, GptResponse.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }
}
