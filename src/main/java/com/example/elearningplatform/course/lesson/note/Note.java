package com.example.elearningplatform.course.lesson.note;

import com.example.elearningplatform.course.lesson.Lesson;
import com.example.elearningplatform.user.user.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "note", uniqueConstraints = @UniqueConstraint(columnNames = { "lesson_id", "user_id" }))
public class Note {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private String content;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
    @ToString.Exclude
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

}