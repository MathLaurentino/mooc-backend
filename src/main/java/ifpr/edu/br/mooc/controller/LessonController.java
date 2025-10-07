package ifpr.edu.br.mooc.controller;

import ifpr.edu.br.mooc.dto.lesson.LessonCreateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonDetailResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonUpdateReqDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LessonController {

    ResponseEntity<LessonDetailResDto> createClass(
            LessonCreateReqDto dto
    );

    ResponseEntity<LessonDetailResDto> updateClass(
            LessonUpdateReqDto dto,
            Long id
    );

    ResponseEntity<LessonDetailResDto> getClassById(
            Long id
    );

    ResponseEntity<List<LessonListResDto>> getClasses(
            Long course_id
    );

}
