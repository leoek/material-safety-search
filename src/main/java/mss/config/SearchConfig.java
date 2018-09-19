package mss.config;

import mss.domain.repository.DataSheetRepository;
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

/**
 * Class for configuring Solr
 */
@Configuration
@EnableSolrRepositories(basePackageClasses = {DataSheetRepository.class})
@ComponentScan
public class SearchConfig {

    @Value("${spring.data.solr.host:http://localhost:8983/solr}")
    private String solrUrl;

    @Value("${solrcore.name:dataSheet}")
    private String solrCoreName;

    /**
     * @return the solrUrl
     */
    public String getSolrUrl() {
        return solrUrl;
    }

    /**
     * @return the solrUrl with the solr core specified
     */
    public String getSolrDataSheetUrl(){
        StringBuilder urlBuilder = new StringBuilder(solrUrl);
        urlBuilder.append("/");
        urlBuilder.append(solrCoreName);
        return urlBuilder.toString();
    }

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
        return new HttpSolrClient.Builder(solrUrl).build();
    }

    @Bean
    SolrClient dataSheetSolrClient() {
        return new HttpSolrClient.Builder(getSolrDataSheetUrl()).build();
    }
}
