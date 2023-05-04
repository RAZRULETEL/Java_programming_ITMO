package Shared.resources;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Objects;
import java.util.Random;

public class Route implements Comparable<Route>, Serializable {
    public static final int MIN_ID = 0;
    public static final int MIN_DISTANCE = 1;
    private static final long serialVersionUID = 3979054390070482918L;

    private int id; //Р—РЅР°С‡РµРЅРёРµ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ Р±РѕР»СЊС€Рµ 0, Р—РЅР°С‡РµРЅРёРµ СЌС‚РѕРіРѕ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ СѓРЅРёРєР°Р»СЊРЅС‹Рј, Р—РЅР°С‡РµРЅРёРµ СЌС‚РѕРіРѕ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ РіРµРЅРµСЂРёСЂРѕРІР°С‚СЊСЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё
    private String name; //РџРѕР»Рµ РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null, РЎС‚СЂРѕРєР° РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ РїСѓСЃС‚РѕР№
    private Coordinates coordinates; //РџРѕР»Рµ РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null
    private java.time.ZonedDateTime creationDate; //РџРѕР»Рµ РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null, Р—РЅР°С‡РµРЅРёРµ СЌС‚РѕРіРѕ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ РіРµРЅРµСЂРёСЂРѕРІР°С‚СЊСЃСЏ Р°РІС‚РѕРјР°С‚РёС‡РµСЃРєРё
    private Location from; //РџРѕР»Рµ РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null
    private Location to; //РџРѕР»Рµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null
    private Integer distance; //РџРѕР»Рµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null, Р—РЅР°С‡РµРЅРёРµ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ Р±РѕР»СЊС€Рµ 1

    {
        creationDate = java.time.ZonedDateTime.ofInstant(Instant.now(), ZoneId.of("GMT+3"));
        id = new Random().nextInt(Integer.MAX_VALUE-MIN_ID)+MIN_ID;
    }

    public Route() {}

    /**
     * Gets the id of the route.
     * @return The id of the route.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the route.
     * @return The name of the route.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the route.
     * @param name The name of the route.
     */
    public void setName(String name) {
        if(name == null || name.equals(""))
            throw new IllegalArgumentException("name РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null, Р»РёР±Рѕ РїСѓСЃС‚РѕР№ СЃС‚СЂРѕРєРѕР№");
        this.name = name;
    }

    /**
     * Gets the coordinates of the route.
     * @return The coordinates of the route.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Sets the coordinates of the route.
     * @param coordinates The coordinates of the route.
     */
    public void setCoordinates(Coordinates coordinates) {
        if(coordinates == null)
            throw new IllegalArgumentException("coordinates РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null");
        this.coordinates = coordinates;
    }

    /**
     * Gets the creation date of the route.
     * @return The creation date of the route.
     */
    public java.time.ZonedDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date of the route.
     * @param creationDate The creation date of the route.
     */
    public void setCreationDate(java.time.ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the from location of the route.
     * @return The from location of the route.
     */
    public Location getFrom() {
        return from;
    }

    /**
     * Sets the from location of the route.
     * @param from The from location of the route.
     */
    public void setFrom(Location from) {
        if(from == null)
            throw new IllegalArgumentException("Location from РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null");
        this.from = from;
    }

    /**
     * Gets the to location of the route.
     * @return The to location of the route.
     */
    public Location getTo() {
        return to;
    }

    /**
     * Sets the to location of the route.
     * @param to The to location of the route.
     */
    public void setTo(Location to) {
        this.to = to;
    }

    /**
     * Gets the distance of the route.
     * @return The distance of the route.
     */
    public Integer getDistance() {
        return distance;
    }

    /**
     * Sets the distance of the route.
     * @param distance The distance of the route.
     */
    public void setDistance(Integer distance) {
        if(distance == null || distance > 1)
            this.distance = distance;
        else throw new IllegalArgumentException("distance РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ Р±РѕР»СЊС€Рµ "+MIN_DISTANCE);
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", from=" + from +
                ", to=" + to +
                ", distance=" + distance +
                '}';
    }

    @Override
    public int compareTo(Route route) {
        return Integer.compare(this.id, route.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return id == route.id && Objects.equals(name, route.name) && Objects.equals(coordinates, route.coordinates) && Objects.equals(creationDate, route.creationDate) && Objects.equals(from, route.from) && Objects.equals(to, route.to) && Objects.equals(distance, route.distance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, from, to, distance);
    }
}
