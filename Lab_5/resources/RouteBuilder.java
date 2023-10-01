package resources;

import java.util.Random;

public class RouteBuilder {
    private String description = null, error = "";
    private boolean isBuilding = true;
    private Route route = new Route(new Random().nextInt(Integer.MAX_VALUE));
    private Object buff;

    public RouteBuilder(Route rt) {
        route = rt;
        isBuilding = false;
    }
    public RouteBuilder() {
        description = "имя";
    }
    public void build(String param){
//        System.out.println(route);
        if(route.getName() == null)
            route.setName(newString(param, false));
        else if(route.getCoordinates() == null)
            route.setCoordinates(newCoords(param));
        else if(route.getFrom() == null)
            route.setFrom(newLocation(param));
        else if (route.getTo() == null)
            route.setTo(newLocation(param));
        else if (route.getDistance() == null)
            route.setDistance(newDistance(param));
        if(route.getDistance() != null)
            isBuilding = false;
    }
    public Route getRoute() {
        if(isBuilding)
            return null;
        return route;
    }
    private String newString(String line, boolean canBeEmpty){
        if(line == null || (canBeEmpty && line.equals(""))) {
            error = "Невалидная строка\n";
            return null;
        }
        nextDescription();
        return line;
    }
    private Integer newDistance(String param){
        Integer dist;
        try {
            dist = Integer.parseInt(param);
        }catch(NumberFormatException ex){
            error = "Неккоректное число\n";
            dist = null;
        }
        if(dist != null && dist <= 1) {
            error = "Неккоректное число\n";
            return null;
        }
        return dist;
    }
    private Coordinates newCoords(String param){
        try {
            if(!(buff instanceof Coordinates) || ((Coordinates)buff).getX() < -819){
                buff = new Coordinates();
                double x = Double.parseDouble(param);
                if(x <= -819)
                    return null;
                ((Coordinates)buff).setX(x);
                nextDescription();
                return null;
            }else{
                int y = Integer.parseInt(param);
                ((Coordinates)buff).setY(y);
                nextDescription();
                return (Coordinates) buff;
            }
        }catch (NumberFormatException ignored){/*return null;*/}
        error = "Неккоректное число\n";
        return null;
    }
    private double newDouble(String param) throws NumberFormatException{
        double doubleValue = Double.parseDouble(param);
        nextDescription();
        return doubleValue;
    }
    private float newFloat(String param) throws NumberFormatException {
        return (float) newDouble(param);
    }
    private Location newLocation(String param){
        try {
            if (!(buff instanceof Location) || ((Location) buff).getName() != null) {
                double x = newDouble(param);
                buff = new Location();
                ((Location) buff).setX(x);
            }else
                switch (description.split(" ")[0]) {
                    case "y" -> ((Location) buff).setY(newFloat(param));
                    case "z" -> ((Location) buff).setZ(newDouble(param));
                    case "name" -> ((Location) buff).setName(newString(param, true));
                }
            if(((Location) buff).getName() != null) {
                Location loc = (Location) buff;
                buff = null;
                return loc;
            }else return null;
        }catch (NumberFormatException ignored){
            error = "Неккоректное число\n";
            return null;}
    }
    public boolean isBuilding() {
        return isBuilding;
    }
    public String getDescription() {
        return error+"Введите "+description+":";
    }
    private static final String[] descriptions = new String[]{"x для Coordinates", "y для Coordinates", "$ для Location", "расстояние"};
    private static final String[] locationFields = new String[]{"x", "y", "z", "name"};
    private int descIndx = -1;
    private void nextDescription(){
        error = "";
        if(descIndx < 0 || (!descriptions[descIndx+1].contains("$") && !descriptions[descIndx].contains("$"))) {
            description = descriptions[++descIndx];
        }else
            if(descriptions[descIndx+1].contains("$"))
                description = descriptions[++descIndx].replace("$", locationFields[0]) + " from";
            else {
                if (route.getFrom() != null && description.split(" ")[0].equals(locationFields[locationFields.length-1]))
                    description = descriptions[++descIndx];
                else {
                    int i = 0;
                    while (i < locationFields.length && !description.split(" ")[0].equals(locationFields[i++]));
//                    System.out.println(Arrays.toString(description.split(" ")));
                    if (i < locationFields.length)
                        description = descriptions[descIndx].replace("$", locationFields[i]);
                    else
                        description = descriptions[descIndx].replace("$", locationFields[0]);
                if(route.getFrom() == null && i != locationFields.length)
                    description += " from";
                else
                    description += " to";
                }
            }
    }

    @Override
    public String toString() {
        return "RouteBuilder{" +
                ", description='" + description + '\'' +
                ", error='" + error + '\'' +
                ", isBuilding=" + isBuilding +
                ", route=" + route +
                ", buff=" + buff +
                ", descIndx=" + descIndx +
                '}';
    }
}
