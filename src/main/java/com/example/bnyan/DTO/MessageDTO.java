package com.example.bnyan.DTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDTO {

    private String fullName;
    private String email;

    @NotEmpty(message = "message subject can not be empty")
    private String subject;
    @NotEmpty(message = "message content can not be empty")
    private String content;
}
