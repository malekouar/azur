package azur.support.web.tool.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link ConfigSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class ConfigSearchRepositoryMockConfiguration {

    @MockBean
    private ConfigSearchRepository mockConfigSearchRepository;

}
