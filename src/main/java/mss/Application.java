package mss;

import mss.domain.entity.DataSheet;
import mss.domain.repository.DataSheetRepository;
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

    @Override
    public void run(String... strings) throws Exception {

        log.info("Done");

        this.dataSheetRepository.deleteAll();

        // insert some products
        DataSheet sheet = new DataSheet();
        sheet.setId(1L);
        sheet.setRaw("some pretty random text");
        this.dataSheetRepository.save(sheet);

        System.exit(0);
    }
}