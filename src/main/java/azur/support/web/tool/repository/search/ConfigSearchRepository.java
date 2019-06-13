package azur.support.web.tool.repository.search;

import azur.support.web.tool.domain.Config;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the {@link Config} entity.
 */
public interface ConfigSearchRepository extends ElasticsearchRepository<Config, Long> {
}
