package com.example.elearningplatform.course.reply;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.Comment;
import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.reply.dto.CreateReplyRequest;
import com.example.elearningplatform.course.reply.dto.ReplyDto;
import com.example.elearningplatform.course.reply.dto.ReplyDtoService;
import com.example.elearningplatform.course.reply.dto.UpdateReplyRequest;
import com.example.elearningplatform.response.Response;
import com.example.elearningplatform.security.TokenUtil;
import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ReplyService {
    private final ReplyRepository replyRepository;
    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ReplyDtoService replyDtoService;

    /************************************************************************************** */
    @Transactional
    public Response createReply(CreateReplyRequest createReply) {

        try {
            User user = userRepository.findById(tokenUtil.getUserId()).orElseThrow();
            Comment comment = commentRepository.findById(createReply.getCommentId()).orElseThrow();
            comment.incrementNumberOfReplies();
            commentRepository.save(comment);
            Reply reply = new Reply(createReply, comment, user);
            // reply.setComment(comment);
            // reply.setContent(createReply.getContent());
            // reply.setCreationDate(LocalDateTime.now());

            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply created successfully",
                    replyDtoService.mapReplyTodDto(reply, false, true));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /************************************************************************************** */

    public Response getRepliesByCommentId(Integer commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new Exception("Comment not found"));
            List<Reply> replyes = replyRepository.findByCommentId(commentId);

            List<Reply> votedComments = replyRepository.findByLikes(tokenUtil.getUserId());

            List<ReplyDto> replyesDto = replyes.stream().map(reply -> {
                Boolean isVotedByUser = votedComments.contains(reply);
                Boolean isCreatedByUser = reply.getUser().getId().equals(tokenUtil.getUserId());
                return replyDtoService.mapReplyTodDto(reply, isCreatedByUser, isVotedByUser);
            }).toList();
            return new Response(HttpStatus.OK, "Replies fetched successfully", replyesDto);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());

        }
    }

    /*************************************************************************************************** */
    public Response deleteReply(Integer replyId) {
        try {
            Reply reply = replyRepository.findById(replyId).orElseThrow();
            Comment comment = commentRepository.findById(reply.getComment().getId()).orElseThrow();
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                return new Response(HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "You are not allowed to delete this reply");
            }

            comment.decrementNumberOfReplies();
            commentRepository.save(comment);
            replyRepository.delete(reply);
            return new Response(HttpStatus.OK, "Reply deleted successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response updateReply(UpdateReplyRequest request) {
        try {
            Reply reply = replyRepository.findById(request.getReplyId()).orElseThrow();
            if (!reply.getUser().getId().equals(tokenUtil.getUserId())) {
                return new Response(HttpStatus.UNAUTHORIZED, "Unauthorized",
                        "You are not allowed to update this reply");
            }
            reply.setContent(request.getContent());
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply updated successfully",
                    replyDtoService.mapReplyTodDto(reply, false, false));
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

    /*************************************************************************************************** */
    public Response likeReply(Integer replyId, Integer userId) {
        try {
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new IllegalArgumentException("Reply not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            reply.addLike(user);
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply liked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }

    }

    /*************************************************************************************************** */

    public Response removeLikeReply(Integer replyId, Integer userId) {
        try {
            Reply reply = replyRepository.findById(replyId)
                    .orElseThrow(() -> new IllegalArgumentException("Reply not found"));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            reply.removeLike(user);
            replyRepository.save(reply);
            return new Response(HttpStatus.OK, "Reply unliked successfully", null);
        } catch (Exception e) {
            return new Response(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
        }
    }

}
