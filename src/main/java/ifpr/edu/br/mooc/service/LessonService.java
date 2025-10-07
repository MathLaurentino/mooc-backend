package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.lesson.LessonCreateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonDetailResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonUpdateReqDto;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.LessonMapper;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper mapper;

    public LessonDetailResDto createLesson(LessonCreateReqDto dto) {
        courseRepository.findById(dto.cursoId()).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        Integer order = lessonRepository.findMaxLessonOrderByCourseId(dto.cursoId())
                .orElse(0) + 1;

        Lesson lesson = mapper.toLesson(dto);
        lesson.setLessonOrder(order);

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapper.toLessonDetailResDto(savedLesson);
    }

    public LessonDetailResDto updateLesson(LessonUpdateReqDto dto, Long id) {
        Lesson lesson = lessonRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Aula não encontrada."));

        mapper.updateLesson(lesson, dto);

        Lesson savedLesson = lessonRepository.save(lesson);

        return mapper.toLessonDetailResDto(savedLesson);
    }

}
