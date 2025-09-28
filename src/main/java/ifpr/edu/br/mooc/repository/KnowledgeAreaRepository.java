package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.KnowledgeArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface KnowledgeAreaRepository extends JpaRepository<KnowledgeArea, Long>, JpaSpecificationExecutor<KnowledgeArea> {

    boolean existsByName(String name);

}
