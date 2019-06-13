package azur.support.web.tool.repository.search;

import azur.support.web.tool.domain.Dossier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Dossier} entity.
 */
public interface DossierSearchRepository extends ElasticsearchRepository<Dossier, Long> {
}
