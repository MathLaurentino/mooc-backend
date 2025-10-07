package ifpr.edu.br.mooc.controller.impl;

import ifpr.edu.br.mooc.controller.LessonController;
import ifpr.edu.br.mooc.dto.lesson.LessonCreateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonDetailResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonUpdateReqDto;
import ifpr.edu.br.mooc.service.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lesson")
@RequiredArgsConstructor
public class LessonControllerImpl implements LessonController {

    private final LessonService service;

    @Override
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LessonDetailResDto> createClass(
            @RequestBody @Valid LessonCreateReqDto dto
    ) {
        var response = service.createLesson(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<LessonDetailResDto> updateClass(LessonUpdateReqDto dto, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<LessonDetailResDto> getClassById(Long id) {
        return null;
    }

    @Override
    public ResponseEntity<List<LessonListResDto>> getClasses(Long course_id) {
        return null;
    }
}
