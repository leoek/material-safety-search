package mss.service;

import com.google.common.base.Splitter;
import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.IngredientDocument;
import mss.domain.repository.DataSheetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class for importing the dataset into Solr
 */
@Service
@Configuration
public class DataSheetImporter {

    @Value("${dataSetPath:./hazard-ds/hazard-dataset}")
    private String dataSetPath;

    @Value("${fscMapPath:./hazard-ds/fscMap.txt}")
    private String fscMapPath;

    @Value("${fsgMapPath:./hazard-ds/fsgMap.txt}")
    private String fsgMapPath;

    @Value("${info:false}")
    private Boolean info;

    @Value("${debug:false}")
    private Boolean debug;

    private List<String> excludes;

    private HashMap<Integer, String> fscMap;
    private HashMap<Integer, String> fsgMap;
    private Set<String> unmappedFscs;
    private Set<String> unmappedFsgs;

    private DataSheetIndexService indexService;

    private DataSheetRepository dataSheetRepository;

    @Autowired
    public DataSheetImporter(DataSheetIndexService indexService, DataSheetRepository dataSheetRepository){
        this.indexService = indexService;
        this.dataSheetRepository = dataSheetRepository;
        excludes = new ArrayList<>();
        excludes.add("index.txt");
        unmappedFscs = new HashSet<>();
        unmappedFsgs = new HashSet<>();
    }

    private static final Logger log = LoggerFactory.getLogger(DataSheetImporter.class);

    /**
     * Starter logic for importing the dataset
     */
    public void importDataSet(){
        if (dataSheetRepository.count() == 0) {
            log.info("No data found. Starting to import from \"" + dataSetPath + "\".");

            fscMap = getFSCMap();
            fsgMap = getFSGMap();

            File baseFolder = new File(dataSetPath);

            if (baseFolder.exists() && baseFolder.isDirectory() && baseFolder.listFiles() != null && baseFolder.listFiles().length > 0){
                importFolder(baseFolder);
                indexService.addRestStillInCache();
                log.info("Import done!");
            } else {
                log.error("Cannot Import Data, Please check that the dataset is available at this location:, {}", dataSetPath);
                log.error("Exiting. There is no data available.");
                System.exit(1);
            }
        } else {
            log.info("Data is already indexed. Wont import again.");
        }

        if (debug){
            if (unmappedFscs.size() > 0){
                log.warn("Couldn't map the following FSCs: {}", unmappedFscs);
            }
            if (unmappedFsgs.size() > 0){
                log.warn("Couldn't map the following FSGs: {}", unmappedFsgs);
            }
        }
    }

    /**
     * Recursive algorithm to call method importFile() on each individual file
     * @param folder File to open / folder to follow
     */
    public void importFolder(File folder){
        if (folder.isFile()){
            importFile(folder);
            return;
        }
        File[] files = folder.listFiles();
        if (files == null) return;
        Arrays.stream(files).forEach(this::importFolder);
    }

    /**
     * Turns a text file into a POJO and sends it to the bulk importer.
     * Also contains logic to enhance document information by mapping FSCG number to corresponding description String.
     * @param file One text file to be analyzed and indexed
     */
    public void importFile(File file){
        if (excludes.contains(file.getName())){
            log.info("Did not import {} (filename is excluded)", file.getName());
            return;
        }
        String rawContent = readFile(file);
        DataSheetDocument document = new DataSheetDocument();

        String idToEncde = file.getParentFile().getName()+"/"+file.getName();
        String id = Base64.getEncoder().encodeToString(idToEncde.getBytes());

        /**
         * Don't use uuids here for now.
         * uuids are not the same through multiple imports or multiple instances
         */
        //document.setId(UUID.randomUUID().toString());
        document.setId(id);
        document.setDocType("datasheet");

        //Get infos from first line
        Pattern p1 = Pattern.compile("(?m)^(.+) -- (.+) {2}-- (.{4}|)-(.+|)$");
        Matcher m1 = p1.matcher(rawContent);
        if (m1.find()){
            String comp = m1.group(1);
            String prod = m1.group(2);
            String fsc = m1.group(3);
            String fsg = "";
            if (m1.group(3).length() >= 4) {
                fsg = m1.group(3).substring(0, 2);
            }
            String niin = m1.group(4);
            document.setCompanyName(comp);
            document.setProductId(prod);
            document.setFsc(fsc);
            document.setFsg(fsg);
            if(fsc.matches("\\d*") && fsc.length() > 2) {
                String fsgString = "";
                String fscString = "";
                try {
                    fsgString = fsgMap.get(Integer.parseInt(fsg));
                    fscString = fscMap.get(Integer.parseInt(fsc));
                } catch (NumberFormatException e){
                    log.error("Tried to parse {} as fsc in file: {} , Got {}", fsc, file.getPath(), e);
                }
                if (fsgString == null || fsgString.equals("")){
                    unmappedFsgs.add(fsg);
                    if (debug) {
                        log.warn("Couldn't set FSG for document {}, fsg: {}", file.getPath(), fsg);
                    }
                    document.setFsgString("FSG Group " + fsg);
                    document.setFsgFacet(document.getFsgString() + "_" + document.getFsg());
                } else {
                    document.setFsgString(fsgString);
                    document.setFsgFacet(document.getFsgString() + "_" + document.getFsg());
                }
                if (fscString == null || fscString.equals("")) {
                    unmappedFscs.add(fsc);
                    if (debug) {
                        log.warn("Couldn't set FSC for document {}, fsc: {}", file.getPath(), fsc);
                    }
                    document.setFscString("FSC Group " + fsc);
                    document.setFscFacet(document.getFscString() + "_" + document.getFsc());
                } else {
                    document.setFscString(fscString);
                    document.setFscFacet(document.getFscString() + "_" + document.getFsc());
                }
            }
            document.setNiin(niin);
        } else {
            log.error("Regex not possible for first line in file " + file.getName() + ". File may be empty.");
        }

        // Get raw infos
        Pattern p2 = Pattern.compile("={4}(?:  |\\t| )(.+)(?:  |\\t| )={4,}(?:\\r\\n|[\\r\\n])(\\X+?(?=(?:====|\\z)))");
        Matcher m2 = p2.matcher(rawContent);
        // Find all matches
        while (m2.find()) {
            // Get the matching string
            String category = m2.group(1);
            String value = m2.group(2).replaceAll("(?:\r\n|[\r\n]){4}", " ").replaceAll("(?:\r\n|[\r\n])\t", " ").trim();
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
            //Normal pattern: 12/17/1986
            Pattern p3 = Pattern.compile("MSDS Date:([0-9]{2}\\/[0-9]{2}\\/[0-9]{4})");
            Matcher m3 = p3.matcher(document.getRawIdentification());
            //Alternative pattern: Jan 24 1989
            Pattern p3alt = Pattern.compile("MSDS Date:([A-Za-z]{3} [0-9]{2} [0-9]{4})");
            Matcher m3alt = p3alt.matcher(document.getRawIdentification());
            if (m3.find()) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    Date date = dateFormat.parse(m3.group(1));
                    document.setMsdsDate(date);
                } catch (ParseException pe) {
                    pe.printStackTrace();
                }
            } else if (m3alt.find()){
                try {
                    DateFormat dateFormat = new SimpleDateFormat("MMM dd yyyy");
                    Date date = dateFormat.parse(m3alt.group(1));
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
            String[] partedIngredients = document.getRawComposition().split("(?:\r\n|[\r\n])(?:\r\n|[\r\n])");

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
                    if (debug){
                        log.warn("Ingredient of file " + file.getName() + " has no name.");
                    }
                }

                //Match CAS
                Pattern p5 = Pattern.compile("CAS:(.*)");
                Matcher m5 = p5.matcher(singleIngredient);
                if (m5.find()){
                    ingredientDocument.setCas(m5.group(1));
                } else {
                    if (debug){
                        log.warn("Ingredient of file " + file.getName() + " has no CAS.");
                    }
                }

                ingredientDocuments.add(ingredientDocument);
            }
        }
        if (debug && ingredientDocuments.size() == 0){
            log.info("No ingredients found for file: " + file.getName());
        }
        document.setIngredients(ingredientDocuments);

        //Send to bulk import
        indexService.addBulk(document);
        if (debug){
            log.info("Imported {} {}", document.getId(), file.getName());
        }
    }

    /**
     * Parses a file to a string and returns it
     * @param file File to be opened
     * @return Content of file
     */
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

    /**
     * Creates a Hashmap connecting FSG numbers to descriptions in order to enhance the information content of the documents
     * @return Hashmap with FSG numbers as keys and descriptions as values
     */
    private HashMap<Integer, String> getFSGMap(){
    	Splitter splitter = Splitter.on(System.getProperty("line.separator")).trimResults().omitEmptyStrings();
    	HashMap<Integer, String> fsgMap = new HashMap<Integer, String>();

    	String fsgMapString = readFile(new File(fsgMapPath));

    	if (fsgMapString == null){
            log.error("FSG Map was not found or is empty. Please check that it is available at: {}", fsgMapPath);
    	    return fsgMap;
        }

    	for (String line : splitter.split(fsgMapString)){
    		int key = Integer.parseInt(line.substring(0, 2));
			String value = line.substring(3,line.length());
            fsgMap.put(key, value);
    	}
    	return fsgMap;
    }

    /**
     * Creates a Hashmap connecting FSC numbers to descriptions in order to enhance the information content of the documents
     * @return Hashmap with FSC numbers as keys and descriptions as values
     */
    private HashMap<Integer, String> getFSCMap(){
	    Splitter splitter = Splitter.on(System.getProperty("line.separator")).trimResults().omitEmptyStrings();
		HashMap<Integer, String> fscMap = new HashMap<Integer, String>();

        String fscMapString = readFile(new File(fscMapPath));

        if (fscMapString == null){
            log.error("FSC Map was not found or is empty. Please check that it is available at: {}", fscMapPath);
            return fscMap;
        }

		for (String line : splitter.split(fscMapString)){
			int key = Integer.parseInt(line.substring(0, 4));
			String value = line.substring(5,line.length());
            fscMap.put(key, value);
		}
		return fscMap;
    }
}
