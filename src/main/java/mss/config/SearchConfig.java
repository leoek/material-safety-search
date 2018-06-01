package mss.config;

import mss.domain.repository.DataSheetRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

@Configuration
@EnableSolrRepositories(basePackageClasses = {DataSheetRepository.class})
public class SearchConfig {
    @Bean
    public SolrServer solrServer(@Value("${spring.data.solr.host}") String solrHost) {
        return new HttpSolrServer(solrHost);
    }

    @Bean
    public SolrServerFactory solrServerFactory(SolrServer solrServer) {
        return new MulticoreSolrServerFactory(solrServer);
    }

    @Bean
    public SolrTemplate solrTemplate(SolrServerFactory solrServerFactory) {
        return new SolrTemplate(solrServerFactory);
    }
}
