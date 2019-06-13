package azur.support.web.tool.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link InterventionSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class InterventionSearchRepositoryMockConfiguration {

    @MockBean
    private InterventionSearchRepository mockInterventionSearchRepository;

}
