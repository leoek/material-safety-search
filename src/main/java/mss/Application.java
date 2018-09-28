package mss;

import mss.service.DataSheetImporter;
import mss.service.SolrSetupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

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

    @Override
    public void run(String... strings) throws Exception {
            solrSetupService.setup();
            dataSheetImporter.importDataSet();
    }
}