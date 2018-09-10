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
        //document.setLines(readFile(file));
        indexService.addBulk(document);
        log.info("Imported {} {}", document.getId(), file.getName());
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

    public void addExampleDoc(){
        DataSheetDocument document = new DataSheetDocument();
        IngredientDocument i1 = new IngredientDocument();
        IngredientDocument i2 = new IngredientDocument();
        i1.setId("0");
        i1.setIngredName("N-BUTYL ACETATE  (SARA III)");
        i1.setCas("123-86-4");

        i2.setId("1");
        i2.setIngredName("2-ETHOXYETHYL ACETATE (CELLOSOLVE ACETATE) (EGEEA)");
        i2.setCas("111-15-9");

        List<IngredientDocument> ingredientDocuments = new ArrayList<>();
        ingredientDocuments.add(i1);
        ingredientDocuments.add(i2);

        //document.setId(0l);
        document.setProductId("SD 36118 GUNSHIP GRAY (822X339)");
        document.setFsc("8010");
        document.setNiin("01-107-2186");
        document.setCompanyName("DESOTO, INC.");
        document.setIngredients(ingredientDocuments);
        DateTimeConverters dateTimeConverters = new DateTimeConverters();



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
