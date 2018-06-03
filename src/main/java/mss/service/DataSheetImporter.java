package mss.service;

import mss.domain.entity.DataSheetDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Configuration
public class DataSheetImporter {

    @Value("${dataSetPath:./hazard-dataset}")
    private String dataSetPath;

    @Value("${info:false}")
    private Boolean info;

    @Value("${debug:false}")
    private Boolean debug;

    private List<String> excludes;

    private DataSheetIndexService indexService;

    @Autowired
    public DataSheetImporter(DataSheetIndexService indexService){
        this.indexService = indexService;
        excludes = new ArrayList<>();
        excludes.add("index.txt");
    }


    private static final Logger log = LoggerFactory.getLogger(DataSheetImporter.class);

    public void importDataSet(){
        importFolder(new File(dataSetPath));
    }

    public void importFolder(File folder){
        if (folder.isFile()){
            importFile(folder);
            return;
        }
        File[] files = folder.listFiles();
        Arrays.stream(files).forEach(file -> importFolder(file));
    }

    public void importFile(File file){
        if (excludes.contains(file.getName())){
            log.info("Did not import {} (filename is excluded):", file.getName());
            return;
        }
        List<String> rawContent = readFile(file);
        DataSheetDocument document = new DataSheetDocument();
        document.setLines(readFile(file));
        indexService.addBulk(document);
        //log.info("Imported {} {}", document.getId(), file.getName());
    }

    private List<String> readFile(File file){
        List<String> lines = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = br.readLine();
            while (line != null) {
                lines.add(line);
                line = br.readLine();
            }
            br.close();
            return lines;
        }catch(Exception e){
            log.error("check the path to the folder! Error: {}", e);
            return null;
        }
    }


}
