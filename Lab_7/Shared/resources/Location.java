package Shared.resources;

import java.io.Serializable;
import java.util.Objects;

public class
Location implements Serializable {
    private static final long serialVersionUID = -1979178303516088180L;
    private double x;
    private float y;
    private double z;
    private String name; //РџРѕР»Рµ РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null

    public Location() {}

    public double getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getName() {
        return name;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public void setName(String name) {
        if(name == null)
            throw new IllegalArgumentException("name РЅРµ РјРѕР¶РµС‚ Р±С‹С‚СЊ null");
        this.name = name;
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Double.compare(location.x, x) == 0 && Float.compare(location.y, y) == 0 && Double.compare(location.z, z) == 0 && Objects.equals(name, location.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }
}
