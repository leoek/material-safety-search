package mss.service;

import mss.domain.entity.DataSheetDocument;
import mss.domain.entity.IngredientDocument;
import mss.service.DataSheetIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.convert.DateTimeConverters;
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
        document.setDocType("datasheet");

        //Get infos from first line

        //log.info(rawContent);
        //String firstLine = rawContent.substring(0, rawContent.indexOf("\n"));
        //log.info("looooooooool " + firstLine);
        Pattern p1 = Pattern.compile("(?m)^(.+) -- (.+) {2}-- (.{4}|)-(.+|)$");
        Matcher m1 = p1.matcher(rawContent);
        if (m1.find()){
            String comp = m1.group(1);
            String prod = m1.group(2);
            String fsc = m1.group(3);
            String niin = m1.group(4);

            //log.info(comp + ", " + prod + ", " + fsc + ", " + niin + "\n");

            document.setCompanyName(comp);
            document.setProductId(prod);
            document.setFsc(fsc);
            document.setNiin(niin);
        } else {
            log.error("Regex not possible for first line in file " + file.getName());
        }

        //Get raw infos
        Pattern p2 = Pattern.compile("={4}(?:  |\\t| )(.+)(?:  |\\t| )={4,}\\n(\\X+?(?=(?:====|\\z)))");
        Matcher m2 = p2.matcher(rawContent);
        // Find all matches
        while (m2.find()) {
            // Get the matching string
            String category = m2.group(1);
            String value = m2.group(2);
            //log.info(category + "\n\n" + value + "\n\n\n");
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
                    document.setRawDisposal(value);
                    break;
                case "Toxicological Information":
                    document.setRawDisposal(value);
                    break;
                case "Ecological Information ":
                    document.setRawDisposal(value);
                    break;
                case "MSDS Transport Information ":
                    document.setRawDisposal(value);
                    break;
                case "Regulatory Information ":
                    document.setRawDisposal(value);
                    break;
                case "Other Information ":
                    document.setRawDisposal(value);
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
                log.warn("No MSDS Date found in file " + file.getName());
            }
        } else {
            log.error("No Product Identification field in file " + file.getName());
            //System.out.print("/hazard-dataset/" + file.getName().substring(0,3) + "/" + file.getName() + ", ");
        }

        //Get ingredients
        List<IngredientDocument> ingredientDocuments = new ArrayList<>();

        if (document.getRawComposition() != null){
            String[] partedIngredients = document.getRawComposition().split("\n\n");

            for (String singleIngredient : partedIngredients) {
                IngredientDocument ingredientDocument = new IngredientDocument();
                ingredientDocument.setDocType("ingredient");
                //TODO: Set ID! Or let it be set somewhere else

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

        if (ingredientDocuments.size() == 0) {
            log.warn("No ingredients found for file: " + file.getName());
        }

        document.setIngredients(ingredientDocuments);

        /*System.out.println("\nPROD: " + document.getProductId());
        System.out.println("FSC: " + document.getFsc());
        System.out.println("NIIN: " + document.getNiin());
        System.out.println("DATE: " + document.getMsdsDate());
        for(IngredientDocument i: document.getIngredients()){
            System.out.println("INGR: " + i.getIngredName() + ", " + i.getCas());
        }
        System.out.println(document.getRawIdentification());*/

        //indexService.addBulk(document);
        //log.info("Imported {} {}", document.getId(), file.getName());
    }

    private String readFile(File file){
        //List<String> lines = new ArrayList<String>();
        String content = "";
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String line = br.readLine();
            while (line != null) {
                content = content + "\n" + line;
                //lines.add(line);
                line = br.readLine();
            }
            br.close();
            return content;
        }catch(Exception e){
            log.error("check the path to the folder! Error: {}", e);
            return null;
        }
    }

    public void addExampleDoc(){
        DataSheetDocument document = new DataSheetDocument();
        IngredientDocument i1 = new IngredientDocument();
        IngredientDocument i2 = new IngredientDocument();
        i1.setId("1");
        i1.setDocType("ingredient");
        i1.setIngredName("N-BUTYL ACETATE  (SARA III)");
        i1.setCas("123-86-4");

        i2.setId("2");
        i2.setDocType("ingredient");
        i2.setIngredName("2-ETHOXYETHYL ACETATE (CELLOSOLVE ACETATE) (EGEEA)");
        i2.setCas("111-15-9");

        List<IngredientDocument> ingredientDocuments = new ArrayList<>();
        ingredientDocuments.add(i1);
        ingredientDocuments.add(i2);

        document.setProductId("SD 36118 GUNSHIP GRAY (822X339)");
        document.setDocType("datasheet");
        document.setFsc("8010");
        document.setNiin("01-107-2186");
        document.setCompanyName("DESOTO, INC.");
        document.setIngredients(ingredientDocuments);

        try {
            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date date = dateFormat.parse("12/13/1983");
            document.setMsdsDate(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        document.setRawIdentification("Product ID:SD 36118 GUNSHIP GRAY (822X339)\n" +
                "MSDS Date:12/13/1983\n" +
                "FSC:8010\n" +
                "NIIN:01-107-2186\n" +
                "MSDS Number: BBHBC\n" +
                "=== Responsible Party ===\n" +
                "Company Name:DESOTO, INC.\n" +
                "Address:1700 S MT PROSPECT RD\n" +
                "City:DES PLAINES\n" +
                "State:IL\n" +
                "ZIP:60018\n" +
                "Info Phone Num:(312) 391-9000\n" +
                "Emergency Phone Num:(312) 391-9000\n" +
                "Preparer's Name:S.M. STEVENSON\n" +
                "CAGE:32758\n" +
                "=== Contractor Identification ===\n" +
                "Company Name:DE SOTO INC\n" +
                "Address:1700 S MT PROSPECT RD\n" +
                "Box:City:DES PLAINES\n" +
                "State:IL\n" +
                "ZIP:60017\n" +
                "Country:US\n" +
                "Phone:708-391-9000\n" +
                "CAGE:96595\n" +
                "Company Name:DESOTO, INC.\n" +
                "Box:5030\n" +
                "CAGE:32758");
        document.setRawComposition("Ingred Name:N-BUTYL ACETATE  (SARA III)\n" +
                "CAS:123-86-4\n" +
                "RTECS #:AF7350000\n" +
                "Fraction by Wt: 10%\n" +
                "Other REC Limits:150 PPM\n" +
                "OSHA PEL:150 PPM/200 STEL\n" +
                "ACGIH TLV:150 PPM/200STEL;9192\n" +
                "EPA Rpt Qty:5000 LBS\n" +
                "DOT Rpt Qty:5000 LBS\n" +
                "\n" +
                "Ingred Name:2-ETHOXYETHYL ACETATE (CELLOSOLVE ACETATE) (EGEEA)\n" +
                "CAS:111-15-9\n" +
                "RTECS #:KK8225000\n" +
                "Fraction by Wt: 25%\n" +
                "OSHA PEL:S,100 PPM\n" +
                "ACGIH TLV:S, 5 PPM; 9192\n" +
                "\n" +
                "Ingred Name:METHYL ISOBUTYL KETONE  (SARA III)\n" +
                "CAS:108-10-1\n" +
                "RTECS #:SA9275000\n" +
                "Fraction by Wt: 10%\n" +
                "Other REC Limits:50 PPM\n" +
                "OSHA PEL:100 PPM/75 STEL\n" +
                "ACGIH TLV:50 PPM/75 STEL; 9293\n" +
                "EPA Rpt Qty:5000 LBS\n" +
                "DOT Rpt Qty:5000 LBS\n" +
                "\n" +
                "Ingred Name:CARBON BLACK\n" +
                "CAS:1333-86-4\n" +
                "RTECS #:FF5800000\n" +
                "Fraction by Wt: <5%\n" +
                "Other REC Limits:3.5 MG/CUM (NIOSH)\n" +
                "OSHA PEL:3.5 MG/M3\n" +
                "ACGIH TLV:3.5 MG/M3; 9192\n" +
                "\n" +
                "Ingred Name:SILICA, AMORPHOUS FUMED (SUSPECTED HUMAN CARCINOGEN BY\n" +
                "    IARC)\n" +
                "CAS:112945-52-5\n" +
                "RTECS #:VV7310000\n" +
                "Fraction by Wt: 15%\n" +
                "OSHA PEL:0.1 MG/CUM\n" +
                "ACGIH TLV:0.1 MG/CUM");
        document.setRawHazards("Routes of Entry: Inhalation:YES  Skin:YES  Ingestion:YES\n" +
                "Reports of Carcinogenicity:NTP:NO    IARC:NO\tOSHA:NO\n" +
                "Health Hazards Acute and Chronic:INHALATION: ANESTHETIC, IRRITAITON OF\n" +
                "    RESPIRATORY TRACT, ACUTE NERVOUS SYSTEM DEPRESSION, HEADACHE,\n" +
                "    DIZZINESS, STAGGERING GAIT, CONFUSION, UNCONSCIOUSNESS, & COMA.\n" +
                "    SKIN/EYES/INGESTION: IRRITATION.\n" +
                "Explanation of Carcinogenicity:NONE\n" +
                "Effects of Overexposure:INHALATION: ANESTHETIC, IRRITATION OF\n" +
                "    RESPIRATORY TRACT, ACUTE NERVOUS SYSTEM DEPRESSION, HEADACHE,\n" +
                "    DIZZINESS, STAGGERING GAIT, CONFUSION, UNCONSCIOUSNESS, & COMA.\n" +
                "    SKIN/EYES/INGESTION: IRRITATION.");
        document.setRawFirstAid("First Aid:INHALATION: REMOVE FROM EXPOSURE. RESTORE BREATHING. CALL\n" +
                "    PHYSICIAN. SKIN: WASH AFFECTED AREA WITH WATER. REMOVE CONTAMINATED\n" +
                "    CLOTHING. EYE: FLUSH WITH WATER FOR 15 MINUTES. CALL A PHYSICIAN.\n" +
                "    INGESTIO N: CONSULT A PHYSICIAN. OBTAIN MEDICAL ATTENTION IN ALL\n" +
                "    CASES.");
        document.setRawFireFighting("Flash Point Method:CC\n" +
                "Flash Point:70F\n" +
                "Lower Limits:1.4%\n" +
                "Extinguishing Media:CLASS B EXTINGUISHER (CO2, DRY CHEMICAL, FOAM)\n" +
                "Fire Fighting Procedures:WATER SPRAY MAY BE INEFFECTIVE. IF WATER IS\n" +
                "    USED, FOG NOZZLES ARE PREFERABLE. FULL PROTECTIVE CLOTHING & SCBA.\n" +
                "    USE WATER TO KEEP FIRE-EXPOSED CONTAINERS COOL.\n" +
                "Unusual Fire/Explosion Hazard:CLOSED CONTAINERS MAY EXPLODE WHEN\n" +
                "    EXPOSED TO EXTTEME HEAT. DON'T APPLY TO HOT SURFACES.");
        document.setRawAccidentalRelease("Spill Release Procedures:REMOVE ALL SOURCES OF IGNITION. AVOID\n" +
                "    BREATHING VAPORS. VENTILATE AREA. REMOVE WITH INERT ABSORBENT &\n" +
                "    NON-SPARKING TOOLS.");
        document.setRawHandlingStorage("Handling and Storage Precautions:DO NOT STORE ABOVE 120F. STORE LARGE\n" +
                "    QUANTITIES IN BUILDINGS DESIGNED FOR STORAGE OF FLAMMABLE LIQUIDS.\n" +
                "    KEEP CONTAINERS TIGHTLY CLOSED.\n" +
                "Other Precautions:DO NOT TAKE INTERNALLY. CONTAINERS SHOULD BE GROUNDED\n" +
                "    WHEN POURING. AVOID FREE FALL OF LIQUID IN EXCESS OF A FEW INCHES.\n" +
                "    DON'T SAND/FLAME CUT/BRAZE OR WELD THE DRY COATING W/O NIOSH\n" +
                "    APPROVED RESPIRATO R/VENTILATION. OBSERVE LABEL WARNINGS.");
        document.setRawProtection("Respiratory Protection:IF >TLV, USE NIOSH APPROVED ORGANIC VAPOR\n" +
                "    RESPIRATOR. IN CONFINED AREAS USE NIOSH APPROVED AIR LINE TYPE\n" +
                "    RESPIRATORS OR HOODS. USE AN APPROVED NIOSH APPROVED RESPIRATOR\n" +
                "    DURING SANDING, FLAME CUT, BRAZ E, OR WELD.\n" +
                "Ventilation:PROVIDE GENERAL DILUTION/LOCAL EXHAUST VENTILATION IN\n" +
                "    VOLUME & PATTERN TO KEEP <TLV.\n" +
                "Protective Gloves:BUTYL RUBBER, NEOPRENE, NITRILE\n" +
                "Eye Protection:SPLASH PROOF GOGGLES, FACE SHIELD\n" +
                "Other Protective Equipment:BUTYL, NEOPRENE, OR NITRILE RUBBER APRON,\n" +
                "    SAFETY SHOWER, EYE BATH, & WASHING FACILITIES\n" +
                "Work Hygienic Practices:REMOVE/LAUNDER CONTAMINATED CLOTHING BEFORE\n" +
                "    REUSE.\n" +
                "Supplemental Safety and Health");
        document.setRawChemicalProperties("Boiling Pt:B.P. Text:242-313F\n" +
                "Vapor Density:>AIR\n" +
                "Evaporation Rate &amp; Reference:SLOWER THAN ETHER\n" +
                "Percent Volatiles by Volume:57.1%");
        document.setRawStabilityReactivity("Stability Indicator/Materials to Avoid:YES\n" +
                "Stability Condition to Avoid:HEAT, ELECTRICAL EQUIPMENT, SPARKS, OPEN\n" +
                "    FLAME & OTHER IGNITION SOURCES.\n" +
                "Hazardous Decomposition Products:PRODUCTS OF COMBUSTION ARE HAZARDOUS.");
        document.setRawDisposal("Waste Disposal Methods:DISPOSE IN ACCORDANCE WITH LOCAL, STATE AND\n" +
                "    FEDERAL REGULATIONS. INCINERATE IN APPROVED FACILITY. DO NOT\n" +
                "    INCINERATE CLOSED CONTAINERS.");


        List<DataSheetDocument> dataSheetDocuments = new ArrayList<>();
        dataSheetDocuments.add(document);

        indexService.add(dataSheetDocuments);
    }


}
