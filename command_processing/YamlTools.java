package command_processing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

import resources.Route;

/**
 * Класс YamlTools предоставляет методы для работы с файлами формата YAML.
 */
public class YamlTools {
    /**
     * Загружает данные из файла формата YAML.
     *
     * @param fileName имя файла
     * @return массив объектов Route
     */
    public static Route[] load(String fileName){
        File file = new File(fileName+".yaml");
        StringBuilder yaml_content = new StringBuilder();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("Такого файла не существует");
            return null;
        }
        while(scanner.hasNextLine())
            yaml_content.append(scanner.nextLine()).append("\n");
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        Route[] collection = null;
        try {
            collection = objectMapper.readValue(yaml_content.toString(), Route[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Application collection info " + Arrays.toString(collection));
        return collection;
    }
    /**
     * Сохраняет данные в файл формата YAML.
     *
     * @param collection массив объектов Route
     * @param fileName имя файла
     * @return true, если сохранение прошло успешно, иначе false
     */
    public static boolean save(Route[] collection, String fileName){
        File file = new File(fileName+".yaml");
        System.out.println(Arrays.toString(collection));
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String content = objectMapper.writeValueAsString(collection);
            fileOutputStream.write(content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Сохраняет данные в файл формата YAML.
     *
     * @param collection коллекция объектов Route
     * @param fileName имя файла
     * @return true, если сохранение прошло успешно, иначе false
     */
    public static boolean save(HashMap<Integer, Route> collection, String fileName){
        Route[] collection_arr = new Route[collection.size()];
        int i = 0;
        for(int key : collection.keySet())
            collection_arr[i++] = collection.get(key);
        File file = new File(fileName+".yaml");
        System.out.println(Arrays.toString(collection_arr));
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            String content = objectMapper.writeValueAsString(collection);
            fileOutputStream.write(content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
