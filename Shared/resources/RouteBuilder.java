package Shared.resources;

import static Shared.resources.Coordinates.MIN_X;
import static Shared.resources.Route.MIN_DISTANCE;

import java.io.InputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RouteBuilder implements Serializable {
    private static final String[] FIELDS_DESCRIPTIONS = new String[]{"name типа % маршрута", "x типа % для Coordinates", "y типа % для Coordinates", "x типа % для Location from", "y типа % для Location from", "z типа % для Location from", "name типа % для Location from", "x типа % для Location to", "y типа % для Location to", "z типа % для Location to", "name типа % для Location to", "расстояние типа %"};

    private String description, error = "", fieldType = "", fieldRequirments = "";
    private Object buff;
    private int descriptionIndex = 0;
    private final Scanner lineReader;

    public RouteBuilder(InputStream inStream) {
        if(inStream != null)
            lineReader = new Scanner(inStream);
        else
            lineReader = new Scanner(System.in);
        description = FIELDS_DESCRIPTIONS[descriptionIndex++];
    }

    public Route build(){
        Route route = new Route();
        route.setName(createString(false));
        route.setCoordinates(createCoords());
        route.setFrom(createLocation(false));
        route.setTo(createLocation(true));
        route.setDistance(createDistance());
        return route;
    }

    private Integer createDistance(){
        fieldRequirments = "больше "+ MIN_DISTANCE;
        Integer dist = createInt(true);
        while(dist != null && dist < MIN_DISTANCE){
            prevDescription();
            fieldRequirments = "больше "+MIN_DISTANCE;
            error = "Число меньше минимального\n";
            dist = createInt(true);
        }
        return dist;
    }
    private Coordinates createCoords(){
        fieldRequirments = "больше "+MIN_X;
        Double x = createDouble(false);
        while(x < MIN_X){
            prevDescription();
            fieldRequirments = "больше "+MIN_X;
            error = "число слишком маленькое\n";
            x = createDouble(false);
        }
        Coordinates coords = new Coordinates();
        coords.setX(x);
        coords.setY(createInt(false));
        return coords;
    }
    private Location createLocation(boolean canBeNull) {
        Double x = createDouble(canBeNull);
        if (x == null){
            nextDescription();
            nextDescription();
            nextDescription();
            return null;
        }
        Location out = new Location();
        out.setX(x);
        out.setY(createFloat(false));
        out.setZ(createDouble(false));
        out.setName(createString(true));
        return out;
    }

    /**
     * Метод для получения полного описания поля, ввод которого запрашивается
     * @return строка, описывающая запрашиваемые данные
     */
    public String getDescription() {
        return error+"Введите "+description.replace("%", fieldType+(!fieldRequirments.equals("")?" "+fieldRequirments:""))+":";
    }
    private void nextDescription(){
        error = "";
        fieldRequirments = "";
        if(descriptionIndex < FIELDS_DESCRIPTIONS.length)
            description = FIELDS_DESCRIPTIONS[descriptionIndex++];
    }
    private void prevDescription() {
        error = "";
        description = FIELDS_DESCRIPTIONS[--descriptionIndex];
    }

    private String createString(boolean canBeEmpty){
        fieldType = "String";
        System.out.println(getDescription());
        String line = readInputLine();
        if(!canBeEmpty && line.equals("")) {
            error = "Невалидная строка\n";
            return createString(canBeEmpty);
        }
        nextDescription();
        return line;
    }
    private Integer createInt(boolean canBeNull){
        fieldType = "int";
        System.out.println(getDescription());
        String line = readInputLine();
        if(canBeNull && line.equals(""))
            return null;
        try{
            int out = Integer.parseInt(line);
            nextDescription();
            return out;
        }catch (NumberFormatException e){
            error = "Неккоректное число\n";
            return createInt(canBeNull);
        }
    }
    private Double createDouble(boolean canBeNull){
        fieldType = "double";
        System.out.println(getDescription());
        String line = readInputLine();
        if(canBeNull && line.equals(""))
            return null;
        try {
            double doubleValue = Double.parseDouble(line);
            nextDescription();
            return doubleValue;
        }catch (NumberFormatException e){
            error = "Неккоректное число\n";
            return createDouble(canBeNull);
        }
    }
    private Float createFloat(boolean canBeNull){
        fieldType = "float";
        System.out.println(getDescription());
        String line = readInputLine();
        if(canBeNull && line.equals(""))
            return null;
        try {
            float floatVal = Float.parseFloat(line);
            nextDescription();
            return floatVal;
        }catch (NumberFormatException e){
            error = "Неккоректное число\n";
            return createFloat(canBeNull);
        }
    }

    /**
     * Метод для чтения следующей строки из потока ввода
     * @return строку из потока переданного в конструктор
     * @throws NoSuchElementException если данные в потоке закончились
     */
    private String readInputLine(){
        if(lineReader.hasNextLine())
            return lineReader.nextLine();
        else throw new NoSuchElementException("Недостаточно данных для создания объекта");
    }

}
