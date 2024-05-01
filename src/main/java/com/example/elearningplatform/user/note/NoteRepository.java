package com.example.elearningplatform.user.note;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.elearningplatform.course.lesson.Lesson;
@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    @Query("""
            SELECT n.lesson FROM Note n
            WHERE n.id = :id
            """)
    Optional<Lesson> findLesson(@Param("id") Integer id);
    Optional<Note> findByLessonId(Integer id);
}
