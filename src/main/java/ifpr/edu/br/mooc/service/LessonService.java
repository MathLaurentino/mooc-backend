package ifpr.edu.br.mooc.service;

import ifpr.edu.br.mooc.dto.lesson.*;
import ifpr.edu.br.mooc.entity.Lesson;
import ifpr.edu.br.mooc.exceptions.base.BadRequestException;
import ifpr.edu.br.mooc.exceptions.base.NotFoundException;
import ifpr.edu.br.mooc.mapper.LessonMapper;
import ifpr.edu.br.mooc.repository.CourseRepository;
import ifpr.edu.br.mooc.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Transactional
    public void reorderLessons(Long courseId, LessonReorderReqDto dto) {
        // 1. Validar se o curso existe
        courseRepository.findById(courseId).orElseThrow(
                () -> new NotFoundException("Curso não encontrado."));

        // 2. Buscar todas as aulas do curso
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByLessonOrderAsc(courseId);

        if (lessons.isEmpty()) {
            throw new BadRequestException("Este curso não possui aulas cadastradas.");
        }

        // 3. Validar quantidade de aulas - TODAS devem estar no DTO
        if (dto.aulas().size() != lessons.size()) {
            throw new BadRequestException(
                    String.format("Esperado %d aulas, mas recebido %d na requisição. " +
                                    "Todas as aulas do curso devem ser incluídas na reordenação.",
                            lessons.size(), dto.aulas().size())
            );
        }

        // 4. Criar sets para validação
        Set<Long> lessonIdsInDatabase = lessons.stream()
                .map(Lesson::getId)
                .collect(Collectors.toSet());

        Set<Long> lessonIdsInDto = dto.aulas().stream()
                .map(LessonReorderReqDto.LessonOrderItem::id)
                .collect(Collectors.toSet());

        // 5. Validar se todos os IDs do DTO pertencem ao curso
        dto.aulas().forEach(item -> {
            if (!lessonIdsInDatabase.contains(item.id())) {
                throw new NotFoundException("Aula com ID " + item.id() + " não encontrada neste curso.");
            }
        });

        // 6. Validar se todas as aulas do curso estão no DTO
        lessonIdsInDatabase.forEach(lessonId -> {
            if (!lessonIdsInDto.contains(lessonId)) {
                throw new BadRequestException("A aula com ID " + lessonId + " do curso está faltando na requisição. " +
                        "Todas as aulas devem ser incluídas.");
            }
        });

        // 7. Validar IDs duplicados na requisição
        if (lessonIdsInDto.size() != dto.aulas().size()) {
            throw new BadRequestException("A lista contém IDs de aulas duplicados.");
        }

        // 8. Validar sequência de ordens (deve ser 1, 2, 3, 4, ...)
        List<Integer> orders = dto.aulas().stream()
                .map(LessonReorderReqDto.LessonOrderItem::ordemAula)
                .sorted()
                .toList();

        for (int i = 0; i < orders.size(); i++) {
            int expectedOrder = i + 1;
            if (!orders.get(i).equals(expectedOrder)) {
                throw new BadRequestException(
                        String.format("Sequência de ordens inválida. Esperado ordem %d, mas encontrado %d. " +
                                        "As ordens devem ser sequenciais (1, 2, 3, ...).",
                                expectedOrder, orders.get(i))
                );
            }
        }

        // 9. Validar ordens duplicadas
        long distinctOrders = dto.aulas().stream()
                .map(LessonReorderReqDto.LessonOrderItem::ordemAula)
                .distinct()
                .count();

        if (distinctOrders != dto.aulas().size()) {
            throw new BadRequestException("A lista contém ordens duplicadas.");
        }

        // 10. Atualizar as ordens
        dto.aulas().forEach(item -> {
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals(item.id()))
                    .findFirst()
                    .orElseThrow(); // Já validado anteriormente
            lesson.setLessonOrder(item.ordemAula());
        });

        // 11. Salvar todas as aulas atualizadas
        lessonRepository.saveAll(lessons);
    }
}
