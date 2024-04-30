package com.example.elearningplatform.user.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.user.note.dto.CreateNoteRequest;
import com.example.elearningplatform.user.note.dto.UpdateNoteRequest;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NoteService {
    @Autowired
    private NoteRepository noteRepository;

    /************************************************************************************************/
    public Response getNotes() {
        try {

            return new Response(HttpStatus.OK, "Note created successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************/
    public Response createNote(CreateNoteRequest request) {
        try {

            return new Response(HttpStatus.OK, "Note created successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************/
    public Response updateNote(UpdateNoteRequest request) {
        try {

            return new Response(HttpStatus.OK, "Note created successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /************************************************************************************************/
    public Response deleteNote(Integer noteId) {
        try {

            return new Response(HttpStatus.OK, "Note created successfully", null);

        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }
    /************************************************************************************************/
}
