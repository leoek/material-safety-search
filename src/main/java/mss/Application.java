package mss;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import mss.service.DataSheetImporter;
import mss.service.DataSheetIndexService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private DataSheetRepository dataSheetRepository;

    @Autowired
    private DataSheetImporter dataSheetImporter;

    @Override
    public void run(String... strings) throws Exception {

        dataSheetImporter.importDataSet();
        log.info("import done");

        

        System.exit(0);
    }
}