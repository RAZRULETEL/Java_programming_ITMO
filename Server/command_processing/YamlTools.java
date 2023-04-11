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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import Server.network_modules.ConnectionManager;
import Shared.resources.Route;

/**
 * Класс YamlTools предоставляет методы для работы с файлами формата YAML.
 */
public class YamlTools {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlTools.class);
    /**
     * Загружает данные из файла формата YAML.
     *
     * @param fileName имя файла
     * @return массив объектов Route
     */
    public static HashMap<Integer, Route> load(String fileName){
        LOGGER.info("Loading collection from file {}", fileName);
        File file = new File(fileName+".yaml");
        StringBuilder yamlContent = new StringBuilder();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Такого файла не существует");
            LOGGER.error(Arrays.toString(e.getStackTrace()));
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
            System.out.println("Файл пустой, либо содержит неккоректные данные");
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }catch (IOException e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            System.out.println("Непредвиденная ошибка чтения файла");
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
     * Сохраняет данные в файл формата YAML.
     *
     * @param collection коллекция объектов Route
     * @param fileName имя файла
     * @return true, если сохранение прошло успешно, иначе false
     */
    public static boolean save(HashMap<Integer, Route> collection, String fileName){
        LOGGER.info("Saving collection to file {}", fileName);
        File file = new File(fileName);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String content = objectMapper.writeValueAsString(collection);
            fileOutputStream.write(content.getBytes());
            return true;
        } catch (IOException e) {
            LOGGER.error(Arrays.toString(e.getStackTrace()));
            return false;
        }
    }
}
