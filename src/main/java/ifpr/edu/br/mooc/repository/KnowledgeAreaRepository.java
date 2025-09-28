package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.KnowledgeArea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeAreaRepository extends JpaRepository<KnowledgeArea, Long> {

    boolean existsByName(String name);

}
