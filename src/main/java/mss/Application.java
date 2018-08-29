package mss;

import mss.domain.entity.DataSheetDocument;
import mss.domain.repository.DataSheetRepository;
import mss.service.DataSheetImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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

        if (dataSheetRepository.count() == 0){
            dataSheetImporter.importDataSet();
            log.info("import done");
        }

        Pageable pageable = PageRequest.of(0, 10);

        Page<DataSheetDocument> documents = dataSheetRepository.findFullText("SEMI GLOSS INTERIOR LATEX WHITE", pageable);

        log.info("{}", documents);

        System.exit(0);
    }
}