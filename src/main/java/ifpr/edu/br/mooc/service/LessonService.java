package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.lesson.LessonCreateReqDto;
import ifpr.edu.br.mooc.dto.lesson.LessonDetailResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonListResDto;
import ifpr.edu.br.mooc.dto.lesson.LessonUpdateReqDto;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.LessonMapper;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper mapper;

    public LessonDetailResDto createLesson(LessonCreateReqDto dto, Long courseId) {
        courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        Integer order = lessonRepository.findMaxLessonOrderByCourseId(courseId)
                .orElse(0) + 1;

        Lesson lesson = mapper.toLesson(dto);
        lesson.setLessonOrder(order);
        lesson.setCourseId(courseId);

        Lesson savedLesson = lessonRepository.save(lesson);
        return mapper.toLessonDetailResDto(savedLesson);
    }

    public LessonDetailResDto updateLesson(LessonUpdateReqDto dto, Long courseId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new NotFoundException("Aula não encontrada."));

        if (!lesson.getCourseId().equals(courseId))
            throw new NotFoundException("Aula não encontrada.");

        mapper.updateLesson(lesson, dto);

        Lesson savedLesson = lessonRepository.save(lesson);

        return mapper.toLessonDetailResDto(savedLesson);
    }

    public LessonDetailResDto getLessonById(Long courseId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new NotFoundException("Aula não encontrada."));

        if (!lesson.getCourseId().equals(courseId))
            throw new NotFoundException("Aula não encontrada.");

        return mapper.toLessonDetailResDto(lesson);
    }

    public List<LessonListResDto> getLessonByCourse(Long courseId) {
        courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId);

        return lessons.stream().map(mapper::toLessonListResDto).toList();
    }

}
