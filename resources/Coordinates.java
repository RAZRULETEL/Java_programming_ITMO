package resources;

import java.util.Objects;

public class Coordinates {
    private double x = -1000; //Значение поля должно быть больше -819
    private int y;

    public Coordinates() {}

    public double getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
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