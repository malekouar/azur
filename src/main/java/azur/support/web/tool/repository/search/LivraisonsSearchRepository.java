package azur.support.web.tool.repository.search;

import azur.support.web.tool.domain.Livraisons;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Livraisons} entity.
 */
public interface LivraisonsSearchRepository extends ElasticsearchRepository<Livraisons, Long> {
}
