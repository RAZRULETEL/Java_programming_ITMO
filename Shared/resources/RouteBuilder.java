package Shared.resources;

import static Shared.resources.Coordinates.MIN_X;
import static Shared.resources.Route.MIN_DISTANCE;

import java.io.InputStream;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RouteBuilder implements Serializable {
    private static final String[] FIELDS_DESCRIPTIONS = new String[]{"name ���� % ��������", "x ���� % ��� Coordinates", "y ���� % ��� Coordinates", "x ���� % ��� Location from", "y ���� % ��� Location from", "z ���� % ��� Location from", "name ���� % ��� Location from", "x ���� % ��� Location to", "y ���� % ��� Location to", "z ���� % ��� Location to", "name ���� % ��� Location to", "���������� ���� %"};

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
        fieldRequirments = "������ "+ MIN_DISTANCE;
        Integer dist = createInt(true);
        while(dist != null && dist < MIN_DISTANCE){
            prevDescription();
            fieldRequirments = "������ "+MIN_DISTANCE;
            error = "����� ������ ������������\n";
            dist = createInt(true);
        }
        return dist;
    }
    private Coordinates createCoords(){
        fieldRequirments = "������ "+MIN_X;
        Double x = createDouble(false);
        while(x < MIN_X){
            prevDescription();
            fieldRequirments = "������ "+MIN_X;
            error = "����� ������� ���������\n";
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
     * ����� ��� ��������� ������� �������� ����, ���� �������� �������������
     * @return ������, ����������� ������������� ������
     */
    public String getDescription() {
        return error+"������� "+description.replace("%", fieldType+(!fieldRequirments.equals("")?" "+fieldRequirments:""))+":";
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
            error = "���������� ������\n";
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
            error = "������������ �����\n";
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
            error = "������������ �����\n";
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
            error = "������������ �����\n";
            return createFloat(canBeNull);
        }
    }

    /**
     * ����� ��� ������ ��������� ������ �� ������ �����
     * @return ������ �� ������ ����������� � �����������
     * @throws NoSuchElementException ���� ������ � ������ �����������
     */
    private String readInputLine(){
        if(lineReader.hasNextLine())
            return lineReader.nextLine();
        else throw new NoSuchElementException("������������ ������ ��� �������� �������");
    }

}
