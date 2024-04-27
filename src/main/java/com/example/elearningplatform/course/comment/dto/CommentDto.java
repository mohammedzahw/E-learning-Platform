package com.example.elearningplatform.course.comment.dto;

import com.example.elearningplatform.user.user.dto.InstructorDto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommentDto {
    private Integer id;
    private Boolean isVotedByUser = false;
    private Boolean isCreatedByUser = false;
    private InstructorDto user;
    private String content;

    private Integer numberOfReplyes = 0;
    private Integer numberOfLikes = 0;

    // public CommentDto(Comment comment, Boolean isVotedByUser, Boolean
    // isCreatedByUser) {

    // if (comment == null)
    // return;
    // this.numberOfReplyes = comment.getNumberOfReplies();
    // this.content=comment.getContent();
    // this.isCreatedByUser=isCreatedByUser;
    // this.isVotedByUser=

    // }

}
