package azur.support.web.tool.repository;

import azur.support.web.tool.domain.Livraisons;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Livraisons entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LivraisonsRepository extends JpaRepository<Livraisons, Long>, JpaSpecificationExecutor<Livraisons> {

}
