package azur.support.web.tool.repository.search;

import azur.support.web.tool.domain.Intervention;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Intervention} entity.
 */
public interface InterventionSearchRepository extends ElasticsearchRepository<Intervention, Long> {
}
