package com.example.elearningplatform.course.comment.dto;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.user.user.dto.UserDtoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentDtoService {
    private final UserDtoService userDtoService;
    public CommentDto mapCommentDto(Comment comment, Boolean isVotedByUser, Boolean isCreatedByUser) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        commentDto.setNumberOfLikes(comment.getNumberOfLikes());
        commentDto.setNumberOfReplyes(comment.getNumberOfReplies());
        commentDto.setIsVotedByUser(isVotedByUser);
        commentDto.setIsCreatedByUser(isCreatedByUser);
        commentDto.setId(comment.getId());
        commentDto.setUser(userDtoService.mapInstructorToDto(comment.getUser()));
       
        return commentDto;
    }
}
