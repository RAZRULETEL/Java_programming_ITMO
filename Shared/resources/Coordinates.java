package Shared.resources;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    public static final int MIN_X = -819;

    private double x; //Р—РЅР°С‡РµРЅРёРµ РїРѕР»СЏ РґРѕР»Р¶РЅРѕ Р±С‹С‚СЊ Р±РѕР»СЊС€Рµ -819
    private int y;

    public Coordinates() {}

    public double getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(double x) {
        if(x > MIN_X)
            this.x = x;
        else throw new IllegalArgumentException("x РґРѕР»Р¶РµРЅ Р±С‹С‚СЊ Р±РѕР»СЊС€Рµ "+MIN_X);
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Double.compare(that.x, x) == 0 && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
