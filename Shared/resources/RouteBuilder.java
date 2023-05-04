package Shared.resources;

import static Shared.resources.Coordinates.MIN_X;
import static Shared.resources.Route.MIN_DISTANCE;

import java.io.InputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class RouteBuilder implements Serializable {
    private static final String[] FIELDS_DESCRIPTIONS = new String[]{"name типа % маршрута", "x типа % для Coordinates", "y типа % для Coordinates", "x типа % для Location from", "y типа % для Location from", "z типа % для Location from", "name типа % для Location from", "x типа % для Location to", "y типа % для Location to", "z типа % для Location to", "name типа % для Location to", "расстояние типа %"};

    private String description, error = "", fieldType = "", fieldRequirments = "";
    private int descriptionIndex = 0;
    private final Scanner lineReader;
    private boolean autoFill = false;

    {
        description = FIELDS_DESCRIPTIONS[descriptionIndex++];
    }

    public RouteBuilder(InputStream inStream) {
        if(inStream != null)
            lineReader = new Scanner(inStream);
        else
            lineReader = new Scanner(System.in);
    }

    /**
     * Blank constructor used to make route with random fields
     */
    public RouteBuilder() {
        lineReader = null;
        autoFill = true;
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
        while(dist != null && dist <= MIN_DISTANCE){
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
        while(x <= MIN_X){
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
        System.out.print(getDescription());
        String line = autoFill ? getRandomString() : readInputLine();
        if(autoFill) {System.out.print(line);
        System.out.println();}
        if(!canBeEmpty && line.equals("")) {
            error = "Невалидная строка\n";
            return createString(canBeEmpty);
        }
        nextDescription();
        return line;
    }
    private Integer createInt(boolean canBeNull){
        fieldType = "int";
        System.out.print(getDescription());
        String line = autoFill ? getRandomInt()+"" : readInputLine();
        if(autoFill){ System.out.print(line);
        System.out.println();}
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
        System.out.print(getDescription());
        String line = autoFill ? getRandomDouble()+"" : readInputLine().replace(",", ".");
        if(autoFill){ System.out.print(line);
        System.out.println();}
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
        System.out.print(getDescription());
        String line = autoFill ? ((float)getRandomDouble())+"" : readInputLine().replace(",", ".");
        if(autoFill){ System.out.print(line);
        System.out.println();}
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
        if(lineReader != null && lineReader.hasNextLine())
            return lineReader.nextLine();
        else throw new NoSuchElementException("Недостаточно данных для создания объекта");
    }

    private String getRandomString(){
        return UUID.randomUUID().toString().substring(new Random().nextInt(36));
    }
    private double getRandomDouble(){
        return new Random().nextDouble();
    }
    private int getRandomInt(){
        return new Random().nextInt();
    }
}
