package com.example.elearningplatform.course.reply.dto;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.reply.Reply;
import com.example.elearningplatform.user.user.dto.UserDtoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyDtoService {
    private final UserDtoService userDtoService;

    public ReplyDto mapReplyTodDto(Reply reply, Boolean isVotedByUser, Boolean isCreatedByUser) {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setIsCreatedByUser(isCreatedByUser);
        replyDto.setIsVotedByUser(isVotedByUser);
        replyDto.setContent(reply.getContent());
        replyDto.setId(reply.getId());
        replyDto.setNumberOfLikes(reply.getNumberOfLikes());
        replyDto.setUser(userDtoService.mapInstructorToDto(reply.getUser()));
        return replyDto;

    }

}
