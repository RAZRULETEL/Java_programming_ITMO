package Server.command_processing;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import Shared.resources.Route;

/**
 * YamlTools used to manipulate YAML data and files.
 */
public class YamlTools {
    private static final Logger logger = LoggerFactory.getLogger(YamlTools.class);
    /**
     *  Used to load collection from yaml file
     *
     * @param fileName file to load
     * @return collection - HashMap<Integer, Route>
     */
    public static HashMap<Integer, Route> load(String fileName){
        logger.info("Loading collection from file {}", fileName);
        File file = new File(fileName+".yaml");
        StringBuilder yamlContent = new StringBuilder();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            logger.error("File not found");
            return null;
        }
        while(scanner.hasNextLine())
            yamlContent.append(scanner.nextLine()).append("\n");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        HashMap<String, Route> collection = null;
        TypeReference<HashMap<String, Route>> typeRef
                = new TypeReference<HashMap<String, Route>>() {};
        try {
            collection = objectMapper.readValue(yamlContent.toString(), typeRef);
        }catch (MismatchedInputException e){
            logger.error("Fail load error");
        }catch (IOException e) {
            logger.error("Fail load error");
        }
        HashMap<Integer, Route> output = new HashMap<Integer, Route>();

        if (collection != null)
            for(String key : collection.keySet()){
                int intKey = Integer.parseInt(key);
                output.put(intKey, collection.get(key));
            }
        System.out.println("Application collection info " + output);

        return output;
    }
    /**
     * ????????? ?????? ? ???? ??????? YAML.
     *
     * @param collection ????????? ???????? Route
     * @param fileName ??? ?????
     * @return true, ???? ?????????? ?????? ???????, ????? false
     */
    public static boolean save(HashMap<Integer, Route> collection, String fileName){
        logger.info("Saving collection to file {}", fileName);
        File file = new File(fileName);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String content = objectMapper.writeValueAsString(collection);
            fileOutputStream.write(content.getBytes());
            return true;
        } catch (IOException e) {
            logger.error("Collection save error");
            return false;
        }
    }
}
