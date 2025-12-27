package com.example.bnyan.Stability;

import com.example.bnyan.DTO.ProjectDTO;
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
}
