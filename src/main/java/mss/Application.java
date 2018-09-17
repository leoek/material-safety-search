package mss;

import mss.config.SearchConfig;
import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import mss.service.DataSheetImporter;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

@SpringBootApplication
@Configuration
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private DataSheetRepository dataSheetRepository;

    @Autowired
    private DataSheetImporter dataSheetImporter;

    @Autowired
    private SearchConfig searchConfig;

    @Autowired
    private SolrClient dataSheetSolrClient;

    public void examples() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<DataSheetDocument> documents = dataSheetRepository.findFullText("SEMI GLOSS INTERIOR LATEX WHITE", pageable);

        log.info("{}", documents);

        //SolrClient solr = new HttpSolrClient.Builder(searchConfig.getSolrDataSheetUrl()).build();

        SolrQuery query = new SolrQuery();
        query.setQuery("productId_s:OXYGEN");
        query.setStart(0);

        try {
            QueryResponse response = dataSheetSolrClient.query(query);
            SolrDocumentList results = response.getResults();
            log.info("{}", results);
        } catch (SolrServerException | IOException solrE){
            log.info("{}", solrE);
        }
    }

    @Override
    public void run(String... strings) throws Exception {

        if (dataSheetRepository.count() == 0){
            dataSheetImporter.importDataSet();
        }
        examples();
        //System.exit(0);
    }
}