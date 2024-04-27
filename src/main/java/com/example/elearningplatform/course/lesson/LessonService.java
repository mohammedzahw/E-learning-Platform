package com.example.elearningplatform.course.lesson;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.elearningplatform.course.comment.CommentRepository;
import com.example.elearningplatform.course.comment.CommentService;
import com.example.elearningplatform.course.lesson.dto.LessonDto;
import com.example.elearningplatform.course.lesson.dto.LessonDtoService;
import com.example.elearningplatform.security.TokenUtil;

import lombok.Data;

@Service
@Data
public class LessonService {
    private final CommentService commentService;
    private final TokenUtil tokenUtil;
    private final LessonRepository lessonRepository;
    private final CommentRepository commentRepository;
    private final LessonDtoService lessonDtoService;

    /*****************************************************************************************************/

    /*****************************************************************************************************/
    public List<LessonDto> getLessonsBySectionId(Integer sectionId) {
        return lessonRepository.findBySectionId(sectionId).stream()
                .map(lesson -> lessonDtoService.mapLessonToDto(lesson)).toList();
    }

    /*****************************************************************************************************/
    public LessonDto getLessonById(Integer id) {

        return lessonDtoService.mapLessonToDto(lessonRepository.findById(id).orElse(null));
    }
    /*****************************************************************************************************/

}
