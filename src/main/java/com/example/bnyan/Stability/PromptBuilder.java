package com.example.bnyan.Stability;

import com.example.bnyan.DTO.ProjectAIDTO;
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
            - When asked in Arabic answer must be in Arabic.
            - The answer must be in one paragraph and no bold test.
            """.formatted(
                question
        );
    }

    public String predictTime(ProjectAIDTO aiDTO){
        return """
            You are a specialist working in constructions projects.
            Your customers will provide you with a build project description, location, land size, start date, and budget.
            You must use the provided information to calculate the time project needs to finish.
            The response must be in JSON format.
            
            Description: %s
            Location: %s
            Land Size: %s
            Start Date: %s
            Budget: %s
            
            Requirements:
            - Return ONLY valid JSON.
            - JSON format: {"expectedProjectPeriod":"...","expectedProjectEndDte":"..."}
            - Expected Period is calculated in days.
            - Expected End Date is a date.
            """.formatted(
                aiDTO.getDescription(),
                aiDTO.getLocation(),
                aiDTO.getLandSize(),
                aiDTO.getStartDate(),
                aiDTO.getBudget()
        );
    }

    public String predictBudget(ProjectAIDTO aiDTO){
        return """
            You are a specialist working in constructions projects.
            Your customers will provide you with a build project description, location, land size, start date.
            You must use the provided information to calculate the budget needed for this project.
            The response must be in JSON format.
            
            Description: %s
            Location: %s
            Land Size: %s
            Start Date: %s
            
            Requirements:
            - Return ONLY valid JSON.
            - JSON format: {"minBudget":"...","maxBudget":"..."}
            - Min Budget and Max Budget are double values.
            """.formatted(
                aiDTO.getDescription(),
                aiDTO.getLocation(),
                aiDTO.getLandSize(),
                aiDTO.getStartDate()
        );
    }

    public String autoFillSpecialistRequest(ProjectAIDTO aiDTO, String specialistType) {
        return """
        You are a specialist working in construction projects. 
        A customer wants to request a specialist for their construction project.
        Based on the project information provided, generate a professional specialist request description and suggest an appropriate offer price.
        
        Project Description: %s
        Project Budget: %s
        Project Location: %s
        Land Size: %s
        Specialist Type: %s
        
        Requirements:
        - Return ONLY valid JSON. 
        - JSON format:  {"description":"... ","offeredPrice":...}
        - The description must be in Arabic (professional and clear).
        - The description should explain what work the specialist needs to do based on the project.
        - The offered price should be reasonable based on the project budget and specialist type.
        - The offered price should be between 5%% to 15%% of the project budget depending on specialist type.
        - offeredPrice must be a number (double).
        """.formatted(
                aiDTO.getDescription(),
                aiDTO.getBudget(),
                aiDTO.getLocation(),
                aiDTO.getLandSize(),
                specialistType
        );
    }
}
