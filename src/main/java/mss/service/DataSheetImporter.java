package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.IngredientDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        indexService.addRestStillInCache();
        log.info("Import done!");
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
        String rawContent = readFile(file);
        DataSheetDocument document = new DataSheetDocument();
        document.setId(UUID.randomUUID().toString());
        document.setDocType("datasheet");

        //Get infos from first line
        Pattern p1 = Pattern.compile("(?m)^(.+) -- (.+) {2}-- (.{4}|)-(.+|)$");
        Matcher m1 = p1.matcher(rawContent);
        if (m1.find()){
            String comp = m1.group(1);
            String prod = m1.group(2);
            String fsc = m1.group(3);
            String niin = m1.group(4);
            document.setCompanyName(comp);
            document.setProductId(prod);
            document.setFsc(fsc);
            document.setNiin(niin);
        } else {
            log.error("Regex not possible for first line in file " + file.getName() + ". File may be empty.");
        }

        // Get raw infos
        Pattern p2 = Pattern.compile("={4}(?:  |\\t| )(.+)(?:  |\\t| )={4,}\\n(\\X+?(?=(?:====|\\z)))");
        Matcher m2 = p2.matcher(rawContent);
        // Find all matches
        while (m2.find()) {
            // Get the matching string
            String category = m2.group(1);
            String value = m2.group(2).replaceAll("\n {4}", " ").replaceAll("\n\t", " ").trim();
            switch (category) {
                case "Product Identification ":
                    document.setRawIdentification(value);
                    break;
                case "Composition/Information on Ingredients ":
                    document.setRawComposition(value);
                    break;
                case "Hazards Identification ":
                    document.setRawHazards(value);
                    break;
                case "First Aid Measures ":
                    document.setRawFirstAid(value);
                    break;
                case "Fire Fighting Measures ":
                    document.setRawFireFighting(value);
                    break;
                case "Accidental Release Measures ":
                    document.setRawAccidentalRelease(value);
                    break;
                case "Handling and Storage ":
                    document.setRawHandlingStorage(value);
                    break;
                case "Exposure Controls/Personal Protection ":
                    document.setRawProtection(value);
                    break;
                case "Physical/Chemical Properties ":
                    document.setRawChemicalProperties(value);
                    break;
                case "Stability and Reactivity Data ":
                    document.setRawStabilityReactivity(value);
                    break;
                case "Disposal Considerations ":
                    document.setRawDisposal(value);
                    break;
                case "Toxicological Information ":
                    document.setRawToxic(value);
                    break;
                case "Toxicological Information":
                    document.setRawToxic(value);
                    break;
                case "Ecological Information ":
                    document.setRawEco(value);
                    break;
                case "MSDS Transport Information ":
                    document.setRawTransport(value);
                    break;
                case "Regulatory Information ":
                    document.setRawRegulatory(value);
                    break;
                case "Other Information ":
                    document.setRawOther(value);
                    break;
                default:
                    log.warn("Unlisted Category \"" + category + "\" found in file " + file.getName() + ".\nPlease consider reporting this to the developers.");
            }
        }

        //Get Date
        if (document.getRawIdentification() != null) {
            Pattern p3 = Pattern.compile("MSDS Date:([0-9]{2}\\/[0-9]{2}\\/[0-9]{4})");
            Matcher m3 = p3.matcher(document.getRawIdentification());
            if (m3.find()) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = dateFormat.parse(m3.group(1));
                    document.setMsdsDate(date);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            } else {
                log.warn("No MSDS Date found or wrong format in file " + file.getName());
            }
        } else {
            log.warn("No Product Identification field in file " + file.getName());
            //For listing paths to corrupted files.
            //System.out.print("/hazard-dataset/" + file.getName().substring(0,3) + "/" + file.getName() + ", ");
        }

        //Get ingredients
        List<IngredientDocument> ingredientDocuments = new ArrayList<>();

        if (document.getRawComposition() != null){
            String[] partedIngredients = document.getRawComposition().split("\n\n");

            for (String singleIngredient : partedIngredients) {
                IngredientDocument ingredientDocument = new IngredientDocument();
                ingredientDocument.setId(UUID.randomUUID().toString());
                ingredientDocument.setDocType("ingredient");

                //Match name
                Pattern p4 = Pattern.compile("Ingred Name:(.*)");
                Matcher m4 = p4.matcher(singleIngredient);
                if (m4.find()){
                    ingredientDocument.setIngredName(m4.group(1));
                } else {
                    //log.info("Ingredient of file " + file.getName() + " has no name.");
                }

                //Match CAS
                Pattern p5 = Pattern.compile("CAS:(.*)");
                Matcher m5 = p5.matcher(singleIngredient);
                if (m5.find()){
                    ingredientDocument.setCas(m5.group(1));
                } else {
                    //log.info("Ingredient of file " + file.getName() + " has no CAS.");
                }

                ingredientDocuments.add(ingredientDocument);
            }
        }

        /*if (ingredientDocuments.size() == 0) {
            log.info("No ingredients found for file: " + file.getName());
        }*/

        document.setIngredients(ingredientDocuments);

        /*System.out.println("\nPROD: " + document.getProductId());
        System.out.println("FSC: " + document.getFsc());
        System.out.println("NIIN: " + document.getNiin());
        System.out.println("DATE: " + document.getMsdsDate());
        for(IngredientDocument i: document.getIngredients()){
            System.out.println("INGR: " + i.getIngredName() + ", " + i.getCas());
        }
        System.out.println(document.getRawIdentification());*/

        indexService.addBulk(document);
        //log.info("Imported {} {}", document.getId(), file.getName());
    }

    private String readFile(File file){
        String content = "";
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = br.readLine();
            while (line != null) {
                content = content + "\n" + line;
                line = br.readLine();
            }
            br.close();
            return content;
        }catch(Exception e){
            log.error("check the path to the folder! Error: {}", e);
            return null;
        }
    }
    }
