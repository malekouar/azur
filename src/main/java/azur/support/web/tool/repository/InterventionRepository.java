package azur.support.web.tool.repository;

import azur.support.web.tool.domain.Intervention;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Intervention entity.
 */
@SuppressWarnings("unused")
@Repository
public interface InterventionRepository extends JpaRepository<Intervention, Long>, JpaSpecificationExecutor<Intervention> {

}
