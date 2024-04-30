package com.example.elearningplatform.user.note.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdateNoteRequest {
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private Integer NoteId;
}
