package project.developer_pacemaker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.developer_pacemaker.dto.gpt.GptDTO;
import project.developer_pacemaker.dto.gpt.GptRequestDTO;
import project.developer_pacemaker.dto.gpt.GptResponseDTO;
import project.developer_pacemaker.service.GptService;

import java.util.List;

@RestController
@RequestMapping("/api/gpt")
public class GptController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @Autowired
    GptService gptService;

    @GetMapping("/ask")
    public String chat(@RequestParam(name = "prompt")String prompt){
        GptRequestDTO request = new GptRequestDTO(model, prompt);
        GptResponseDTO chatGPTResponse =  template.postForObject(apiURL, request, GptResponseDTO.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @GetMapping("/list")
    public ResponseEntity<?> getMyGptList(@RequestParam String nickname){
        // 임시로 닉네임
        List<GptDTO> gptList = gptService.getGpt(nickname);
        return new ResponseEntity<>(gptList, HttpStatus.OK);
    }
}
