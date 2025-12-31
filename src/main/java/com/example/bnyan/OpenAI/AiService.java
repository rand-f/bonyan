package com.example.bnyan.OpenAI;


import com.example.bnyan.DTO.*;
import com.example.bnyan.Stability.PromptBuilder;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final PromptBuilder promptBuilder;
    private final AIResponseParser aiResponseParser;

    @Value("${openai.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.openai.com/v1/responses")
            .build();

    public String useAI(String prompt) {
        Map<String, Object> message = Map.of(
                "role", "user",
                "content", prompt
        );

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-4o-mini",
                "input", List.of(message)
        );

        String result = webClient.post()
                .uri("https://api.openai.com/v1/responses")
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println("\n\n================ RAW AI RESPONSE ================\n");
        System.out.println(result);
        System.out.println("\n=================================================\n");

        return result;
    }

    public QuestionDTO askAI(String question){
        String prompt = promptBuilder.askAI(question);
        String response= useAI(prompt);
        QuestionDTO answer = aiResponseParser.generatedAnswerOpenAi(response);
        answer.setQuestion(question);
        return answer;
    }

    public PredictionBudgetDTO predictBudget(ProjectAIDTO aiDTO){
        String prompt = promptBuilder.predictBudget(aiDTO);
        String response = useAI(prompt);
        return aiResponseParser.generatedBudgetOpenAi(response);
    }

    public PredictionTimeDTO predictTime(ProjectAIDTO aiDTO){
        String prompt = promptBuilder.predictTime(aiDTO);
        String response = useAI(prompt);
        return aiResponseParser.generatedTimeOpenAi(response);
    }

    public SpecialistRequestAutoFillDTO autoFillSpecialistRequest(ProjectAIDTO aiDTO, String specialistType) {
        String prompt = promptBuilder.autoFillSpecialistRequest(aiDTO, specialistType);
        String response = useAI(prompt);
        return aiResponseParser.generatedAutoFillOpenAi(response);
    }

}
