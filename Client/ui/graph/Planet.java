package Client.ui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import Client.ui.interfaces.PlanetChangeListener;
import Shared.resources.Route;

public class Planet {
    public static final int BORDER_WIDTH = 2;
    private static final double GRAVITATIONAL_CONSTANT = 6.67 * Math.pow(10, -11), BORDER_SLOWING_COEFFICIENT = 1.1, MAX_SPEED = 100;//Need to prevent infinite speed growing
    private int mass;
    private final int startOffsetX, startOffsetY, paneWidth, paneHeight, maxRadius;
    private int radius;
    private float speedX = 0, speedY = 0;
    private double x, y;
    private Color color, borderColor;
    private Route origin;
    private final ArrayList<PlanetChangeListener> listeners = new ArrayList<>();

    public Planet(Route obj, Color color, int startOffsetX, int startOffsetY, int width, int height, int maxRadius) {
        this.x = obj.getFrom().getX();
        this.y = obj.getFrom().getZ();
        this.mass = obj.getDistance() == null ? 0 : obj.getDistance();
        this.radius = (int) Math.max(1, (Integer.MAX_VALUE / 2.0 + obj.getCoordinates().getY() / 2.0) / Integer.MAX_VALUE * maxRadius);
        this.maxRadius = maxRadius;
        this.startOffsetX = startOffsetX;
        this.startOffsetY = startOffsetY;
        this.color = color;
        this.paneWidth = width;
        this.paneHeight = height;
        origin = obj;
    }

    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(startOffsetX + getXOnPane() - radius, startOffsetY + getYOnPane() - radius, radius * 2, radius * 2);
        if (borderColor != null) {
            g.setStroke(new BasicStroke(BORDER_WIDTH));
            g.setColor(borderColor);
            g.drawOval(startOffsetX + getXOnPane() - radius, startOffsetY + getYOnPane() - radius, radius * 2, radius * 2);
        }
    }

    public int getMass() {
        return mass;
    }

    public void addSpeedVector(float x, float y) {
        speedX = (float) (speedX + x > 0 ? Math.min(speedX + x, MAX_SPEED) : Math.max(speedX + x, -MAX_SPEED));
        speedY = (float) (speedY + y > 0 ? Math.min(speedY + y, MAX_SPEED) : Math.max(speedY + y, -MAX_SPEED));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getXOnPane() {
        return (int) (paneWidth * ((Double.MAX_VALUE / 2 + x / 2) / Double.MAX_VALUE));
    }

    public int getYOnPane() {
        return (int) (paneHeight * ((Double.MAX_VALUE / 2 + y / 2) / Double.MAX_VALUE));
    }

    public double getDistance(Planet planet) {
        return Math.sqrt(Math.pow((planet.getXOnPane() - getXOnPane()), 2) + Math.pow((planet.getYOnPane() - getYOnPane()), 2));
    }

    public double getDistance(Point point) {
        return Math.sqrt(Math.pow((point.getX() - getXOnPane()), 2) + Math.pow((point.getY() - getYOnPane()), 2));
    }


    public void interact(Planet planet) {
        double dist = Math.max(1, getDistance(planet));
        double power = GRAVITATIONAL_CONSTANT * Math.max(planet.mass, 1) * Math.max(mass, 1) / dist;

        if (dist <= radius + planet.radius) {
            power = -power;
            handleCollision(planet);
        }
        float speedX0 = (float) (power * ((getXOnPane() - planet.getXOnPane()) / dist) / Math.max(planet.mass, 1));
        float speedY0 = (float) (power * ((getYOnPane() - planet.getYOnPane()) / dist) / Math.max(planet.mass, 1));

        float speedX1 = (float) (power * ((planet.getXOnPane() - getXOnPane()) / dist) / Math.max(mass, 1));
        float speedY1 = (float) (power * ((planet.getYOnPane() - getYOnPane()) / dist) / Math.max(mass, 1));

        planet.addSpeedVector(speedX0, speedY0);
        this.addSpeedVector(speedX1, speedY1);
    }

    private void handleCollision(Planet planet) {
        double angle = Math.atan2(-planet.getYOnPane() + getYOnPane(), planet.getXOnPane() - getXOnPane());

        double speedVec1 = Math.sqrt(speedX * speedX + speedY * speedY);
        double speedVec2 = Math.sqrt(planet.speedX * planet.speedX + planet.speedY * planet.speedY);

        double direction1 = Math.atan2(speedY, speedX);
        double direction2 = Math.atan2(planet.speedY, planet.speedX);

        double newSpeedX1 = speedVec1 * Math.cos(direction1 - angle);
        double newSpeedY1 = speedVec1 * Math.sin(direction1 - angle);

        double newSpeedX2 = speedVec2 * Math.cos(direction2 - angle);
        double newSpeedY2 = speedVec2 * Math.sin(direction2 - angle);

        double massSum = Math.max((mass + 0.0 + planet.mass), 1);
        double mass1 = Math.max(mass, 1);
        double mass2 = Math.max(planet.mass, 1);
        double finalSpeedX1 = ((mass1 - mass2) * newSpeedX1 + (mass2 * 2.0) * newSpeedX2) / massSum;
        double finalSpeedX2 = ((mass1 * 2.0) * newSpeedX1 + (mass2 - mass1) * newSpeedX2) / massSum;

        double finalSpeedY1 = ((mass1 - mass2) * newSpeedY1 + (mass2 * 2.0) * newSpeedY2) / massSum;
        double finalSpeedY2 = ((mass1 * 2.0) * newSpeedY1 + (mass2 - mass1) * newSpeedY2) / massSum;

        speedX = (float) (Math.cos(angle) * finalSpeedX1 + Math.cos(angle + Math.PI / 2) * finalSpeedY1);
        speedY = (float) (Math.sin(angle) * finalSpeedX1 + Math.sin(angle + Math.PI / 2) * finalSpeedY1);

        planet.speedX = (float) (Math.cos(angle) * finalSpeedX2 + Math.cos(angle + Math.PI / 2) * finalSpeedY2);
        planet.speedY = (float) (Math.sin(angle) * finalSpeedX2 + Math.sin(angle + Math.PI / 2) * finalSpeedY2);
    }

    public void tick() {
        synchronized (this) {
            x += speedX * Double.MAX_VALUE;
            y += speedY * Double.MAX_VALUE;
            if (Math.abs(x) == Double.POSITIVE_INFINITY) {
                x = Double.MAX_VALUE * (x < 0 ? -1 : 1);
                speedX = (float) (-speedX / BORDER_SLOWING_COEFFICIENT);
            }
            if (Math.abs(y) == Double.POSITIVE_INFINITY) {
                y = Double.MAX_VALUE * (y < 0 ? -1 : 1);
                speedY = (float) (-speedY / BORDER_SLOWING_COEFFICIENT);
            }
            origin.getFrom().setX(this.x);
            origin.getFrom().setZ(this.y);
        }
        fireChangeEvent();
    }

    public void setCoordinates(int x, int y) {
        synchronized (this) {
            this.x = (((double) x) / paneWidth - 0.5) * 2 * Double.MAX_VALUE;
            this.y = (((double) y) / paneHeight - 0.5) * 2 * Double.MAX_VALUE;
            origin.getFrom().setX(this.x);
            origin.getFrom().setZ(this.y);
        }
        fireChangeEvent();
    }

    public void setMass(int mass) {
        if (mass < 0)
            throw new IllegalArgumentException("Mass cannot be negative");
        this.mass = mass;
        origin.setDistance(mass == 0 ? null : mass);
        fireChangeEvent();
    }

    public Route getRoute() {
        return origin;
    }

    public void setBorderColor(Color color) {
        this.borderColor = color;
    }

    private void fireChangeEvent() {
        for (PlanetChangeListener listener : listeners)
            listener.onChange(this);
    }

    public void clearBorderColor() {
        borderColor = null;
    }

    public void addOnChangeListener(PlanetChangeListener listener) {
        listeners.add(listener);
    }

    public void updateByRoute(Route route) {
        synchronized (this) {
            this.origin = route;
            this.x = route.getFrom().getX();
            this.y = route.getFrom().getZ();
            this.mass = route.getDistance() == null ? 0 : route.getDistance();
            this.radius = (int) Math.max(1, (Integer.MAX_VALUE / 2.0 + route.getCoordinates().getY() / 2.0) / Integer.MAX_VALUE * maxRadius);
        }
    }

    public int getMaxRadius() {
        return maxRadius;
    }

    public void setRadius(int radius) {
        if (radius < 0 || radius > maxRadius)
            throw new IllegalArgumentException("Radius must be between 0 and maxRadius(" + maxRadius + ")");
        origin.getCoordinates().setY((int) ((radius / (maxRadius + 0.0) * 2 - 1) * Integer.MAX_VALUE));
        this.radius = radius;
        fireChangeEvent();
    }

    public int getRadius() {
        return radius;
    }

    public int getSourceRadius() {
        return origin.getCoordinates().getY();
    }

    public Planet getState(){
        Planet planet = new Planet(origin, color, startOffsetX, startOffsetY, paneWidth, paneHeight, maxRadius);
        planet.speedX = speedX;
        planet.speedY = speedY;
        planet.x = x;
        planet.y = y;
        return planet;
    }
}
