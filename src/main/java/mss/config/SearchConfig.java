package mss.config;

import mss.domain.repository.DataSheetRepository;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.data.solr.server.SolrClientFactory;
import org.springframework.data.solr.server.support.HttpSolrClientFactory;

@Configuration
@EnableSolrRepositories(basePackageClasses = {DataSheetRepository.class})
@ComponentScan
public class SearchConfig {
    @Bean
    SolrTemplate solrTemplate() {
        return new SolrTemplate(solrClientFactory());
    }

    @Bean
    SolrClientFactory solrClientFactory() {
        //Credentials credentials = new UsernamePasswordCredentials("solr", "SolrRocks");
        //return new HttpSolrClientFactory(solrClient(), "merchant_core", credentials , "BASIC");
        return new HttpSolrClientFactory(solrClient());
    }

    @Bean
    SolrClient solrClient() {
        return new HttpSolrClient.Builder("http://localhost:8983/solr").build();
    }
}
