package com.example.bnyan.OpenAI;

import com.example.bnyan.Api.ApiException;
import com.example.bnyan.DTO.PredictionBudgetDTO;
import com.example.bnyan.DTO.PredictionTimeDTO;
import com.example.bnyan.DTO.QuestionDTO;
import com.example.bnyan.DTO.SpecialistRequestAutoFillDTO;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Component
public class AIResponseParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public QuestionDTO generatedAnswerOpenAi(String question) {

        QuestionDTO answer = new QuestionDTO();

        try {
            JsonNode root = mapper.readTree(question);
            JsonNode jsonContent;

            if (root.has("output")) {
                JsonNode output = root.get("output");

                String text = output.get(0)
                        .get("content")
                        .get(0)
                        .get("text")
                        .asText();

                String clean = text
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                jsonContent = mapper.readTree(clean);
            }

            else if (root.has("choices")) {
                String text = root.get("choices")
                        .get(0)
                        .get("message")
                        .get("content")
                        .asText();

                jsonContent = mapper.readTree(text);
            }

            else if (root.isObject()) {
                jsonContent = root;
            }

            else {
                throw new ApiException("AI response format not recognized");
            }

            answer.setAnswer(jsonContent.has("answer") ? jsonContent.get("answer").asText() : null);

            return answer;

        } catch (Exception e) {
            throw new ApiException("Error parsing AI response: " + e.getMessage());
        }
    }

    public PredictionBudgetDTO generatedBudgetOpenAi(String response) {

        PredictionBudgetDTO budget = new PredictionBudgetDTO();

        try {
            JsonNode root = mapper.readTree(response);
            JsonNode jsonContent;

            if (root.has("output")) {
                JsonNode output = root.get("output");

                String text = output.get(0)
                        .get("content")
                        .get(0)
                        .get("text")
                        .asText();

                String clean = text
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                jsonContent = mapper.readTree(clean);
            }

            else if (root.has("choices")) {
                String text = root.get("choices")
                        .get(0)
                        .get("message")
                        .get("content")
                        .asText();

                jsonContent = mapper.readTree(text);
            }

            else if (root.isObject()) {
                jsonContent = root;
            }

            else {
                throw new ApiException("AI response format not recognized");
            }

            budget.setMinBudget(jsonContent.has("minBudget") ? jsonContent.get("minBudget").asDouble() : null);
            budget.setMaxBudget(jsonContent.has("maxBudget") ? jsonContent.get("maxBudget").asDouble() : null);

            return budget;

        } catch (Exception e) {
            throw new ApiException("Error parsing AI response: " + e.getMessage());
        }
    }

    public PredictionTimeDTO generatedTimeOpenAi(String response) {

        PredictionTimeDTO time = new PredictionTimeDTO();

        try {
            JsonNode root = mapper.readTree(response);
            JsonNode jsonContent;

            if (root.has("output")) {
                JsonNode output = root.get("output");

                String text = output.get(0)
                        .get("content")
                        .get(0)
                        .get("text")
                        .asText();

                String clean = text
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                jsonContent = mapper.readTree(clean);
            }

            else if (root.has("choices")) {
                String text = root.get("choices")
                        .get(0)
                        .get("message")
                        .get("content")
                        .asText();

                jsonContent = mapper.readTree(text);
            }

            else if (root.isObject()) {
                jsonContent = root;
            }

            else {
                throw new ApiException("AI response format not recognized");
            }

            time.setExpectedProjectPeriod(jsonContent.has("expectedProjectPeriod") ? jsonContent.get("expectedProjectPeriod").asInt() : null);
            time.setExpectedProjectEndDte(jsonContent.has("expectedProjectEndDte") ? jsonContent.get("expectedProjectEndDte").asText(): null);

            return time;

        } catch (Exception e) {
            throw new ApiException("Error parsing AI response: " + e.getMessage());
        }
    }

    public SpecialistRequestAutoFillDTO generatedAutoFillOpenAi(String response) {

        SpecialistRequestAutoFillDTO autoFill = new SpecialistRequestAutoFillDTO();

        try {
            JsonNode root = mapper. readTree(response);
            JsonNode jsonContent;

            if (root.has("output")) {
                JsonNode output = root.get("output");

                String text = output.get(0)
                        .get("content")
                        .get(0)
                        .get("text")
                        .asText();

                String clean = text
                        .replace("```json", "")
                        .replace("```", "")
                        .trim();

                jsonContent = mapper.readTree(clean);
            }

            else if (root.has("choices")) {
                String text = root. get("choices")
                        . get(0)
                        . get("message")
                        . get("content")
                        . asText();

                jsonContent = mapper.readTree(text);
            }

            else if (root.isObject()) {
                jsonContent = root;
            }

            else {
                throw new ApiException("AI response format not recognized");
            }

            autoFill.setDescription(jsonContent.has("description") ? jsonContent.get("description").asText() : null);
            autoFill.setOfferedPrice(jsonContent.has("offeredPrice") ? jsonContent.get("offeredPrice").asDouble() : null);

            return autoFill;

        } catch (Exception e) {
            throw new ApiException("Error parsing AI response: " + e. getMessage());
        }
    }
}
