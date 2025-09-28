package ifpr.edu.br.mooc.controller.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/course")
public class CourseControllerImpl {

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Void> createCourse(){
        System.out.println("Criar curso é limitado para usuario adm");
        return ResponseEntity.ok().build();
    }

}
