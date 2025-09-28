package ifpr.edu.br.mooc.repository;

import ifpr.edu.br.mooc.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CampusRepository extends JpaRepository<Campus, Long>, JpaSpecificationExecutor<Campus> {

    boolean existsByName(String name);

}
