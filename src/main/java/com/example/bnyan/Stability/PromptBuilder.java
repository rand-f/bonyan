package com.example.bnyan.Stability;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String generateImagePrompt(String description){
        return """
            The image must be an EXTERNAL VIEW of the described building.
            Must be a clear full building.
            Description: %s
            """.formatted(
            description
        );
    }

    public String askAI(String question){
        return """
            You are a specialist working in constructions projects.
            Your customers will ask you constructing related questions.
            You must advice them in a clear and simple way assuming they do not have knowledge in this field.
            The answer must be in JSON format.
            Description: %s
            Requirements:
            - Return ONLY valid JSON.
            - JSON format: {"answer":"..."}
            """.formatted(
                question
        );
    }

    public String predictTime(String question){
        return """
            You are a specialist working in constructions projects.
            Your customers will provide you with a project description and project 
            You must advice them in a clear and simple way assuming they do not have knowledge in this field.
            The answer must be in JSON format.
            Description: %s
            Requirements:
            - Return ONLY valid JSON.
            - JSON format: {"answer":"..."}
            """.formatted(
                question
        );
    }

    public String predictBudget(String question){
        return """
            You are a specialist working in constructions projects.
            Your customers will ask you constructing related questions.
            You must advice them in a clear and simple way assuming they do not have knowledge in this field.
            The answer must be in JSON format.
            Description: %s
            Requirements:
            - Return ONLY valid JSON.
            - JSON format: {"answer":"..."}
            """.formatted(
                question
        );
    }
}
