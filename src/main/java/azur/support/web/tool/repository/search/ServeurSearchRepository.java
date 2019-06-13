package azur.support.web.tool.repository.search;

import azur.support.web.tool.domain.Serveur;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Serveur} entity.
 */
public interface ServeurSearchRepository extends ElasticsearchRepository<Serveur, Long> {
}
