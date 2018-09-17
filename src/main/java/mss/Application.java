package mss;

import mss.config.SearchConfig;
import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import mss.service.DataSheetImporter;
import mss.service.SolrSetupService;
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
    private DataSheetImporter dataSheetImporter;

    @Autowired
    private SolrSetupService solrSetupService;

    private SearchConfig searchConfig;

    @Override
    public void run(String... strings) throws Exception {
            solrSetupService.setup();
            dataSheetImporter.importDataSet();
    }
}