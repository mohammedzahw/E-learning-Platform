package com.example.elearningplatform.course.comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.elearningplatform.course.comment.dto.CreateCommentRequest;
import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.user.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@RequiredArgsConstructor
@Table(name = "Comment", indexes = {

        @Index(name = "lesson_comment_id_index", columnList = "lesson_id", unique = false),
        @Index(name = "user_comment_id_index", columnList = "user_id", unique = false)
})
public class Comment  {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String content;
    private LocalDateTime creationDate;
    private LocalDateTime modificationDate;

  

    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;


    @ManyToOne(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    @ManyToMany(fetch = jakarta.persistence.FetchType.EAGER)
    @ToString.Exclude
    @JoinTable(name = "comment_likes", joinColumns = @JoinColumn(name = "comment_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    List<User> likes = new ArrayList<>();

    private Integer numberOfReplies = 0;

    private Integer numberOfLikes = 0;

    public void addLike(User user) {
        numberOfLikes++;
        likes.add(user);
    }

    public void removeLike(User user) {
        numberOfLikes--;
        likes.remove(user);
    }

    public void incrementNumberOfReplies() {

        this.numberOfReplies++;
    }

    public void decrementNumberOfReplies() {
        this.numberOfReplies--;
    }

    public Comment(CreateCommentRequest createComment, User user, Lesson lesson) {
        
        this.lesson = lesson;
        this.creationDate = LocalDateTime.now();
        this.modificationDate = LocalDateTime.now();
        this.user = user;
        this.content = createComment.getContent();


    }

    // public Comment() {
    //     super();
    // }

}
