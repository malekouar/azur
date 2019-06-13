package azur.support.web.tool.repository;

import azur.support.web.tool.domain.Dossier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Dossier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DossierRepository extends JpaRepository<Dossier, Long>, JpaSpecificationExecutor<Dossier> {

}
