package ifpr.edu.br.mooc.exceptions.lesson;

import ifpr.edu.br.mooc.exceptions.base.BadRequestException;

public class LessonNotBelongsToCourseException extends BadRequestException {
    
    public LessonNotBelongsToCourseException() {
        super("A aula não pertence ao curso desta inscrição.");
    }
    
    public LessonNotBelongsToCourseException(Long lessonId, Long courseId) {
        super(String.format("A aula com ID %d não pertence ao curso com ID %d.", lessonId, courseId));
    }
}