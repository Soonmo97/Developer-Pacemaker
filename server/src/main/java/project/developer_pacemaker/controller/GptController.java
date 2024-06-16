package project.developer_pacemaker.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import project.developer_pacemaker.dto.gpt.*;
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

    @Operation(summary = "gpt 질문", description = "gpt 질문 API 입니다.")
    @PostMapping("/ask")
    public String chat(@RequestBody GptPromptDTO prompt){
        GptRequestDTO request = new GptRequestDTO(model, prompt.getPrompt());
        GptResponseDTO chatGPTResponse =  template.postForObject(apiURL, request, GptResponseDTO.class);
        return chatGPTResponse.getChoices().get(0).getMessage().getContent();
    }

    @Operation(summary = "gpt 리스트 조회", description = "gpt 리스트 조회 API 입니다.")
    @GetMapping()
    public ResponseEntity<?> getMyGptList(@AuthenticationPrincipal String uSeq){
        Long uSeqLong = Long.parseLong(uSeq);
        List<GptControlDTO> gptList = gptService.getGptListByUSeq(uSeqLong);
        return new ResponseEntity<>(gptList, HttpStatus.OK);
    }

    @Operation(summary = "gpt 저장", description = "gpt 저장 API 입니다.")
    @PostMapping()
    public ResponseEntity<String> saveGpt(@AuthenticationPrincipal String uSeq, @RequestBody GptDTO gptDto){
        Long uSeqLong = Long.parseLong(uSeq);
        gptService.saveGpt(uSeqLong, gptDto);

        return new ResponseEntity<>("Gpt question and answer saved successfully", HttpStatus.CREATED);
    }

    @Operation(summary = "gpt 수정", description = "gpt 수정 API 입니다.")
    @PatchMapping("/{gSeq}")
    public ResponseEntity<?> deleteGpt(@PathVariable("gSeq") long gSeq) {
        boolean deleted = gptService.deleteGptByGSeq(gSeq);
        if (deleted) {
            return ResponseEntity.ok("Deletion successful");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Failed to delete gpt data");
        }
    }

    @Operation(summary = "gpt 삭제 리스트 조회", description = "gpt 삭제 리스트 조회 API 입니다.")
    @GetMapping("/deleted")
    public ResponseEntity<?> getDeletedGptList(@AuthenticationPrincipal String uSeq){
        Long uSeqLong = Long.parseLong(uSeq);
        List<GptDTO> gptList = gptService.getDeletedGptListByUser(uSeqLong);
        return new ResponseEntity<>(gptList, HttpStatus.OK);
    }

}
