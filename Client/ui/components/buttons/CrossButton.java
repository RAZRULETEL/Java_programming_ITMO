package Client.ui.components.buttons;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.BorderFactory;
import javax.swing.JButton;

public class CrossButton extends JButton {
    private int borderWidth = 1;
    public CrossButton(int size){
        setPreferredSize(new Dimension(size, size));
        setSize(new Dimension(size, size));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setOpaque(false);
    }

    @Override//Meaningless method
    protected void paintComponent(Graphics g) {}

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(getBackground());
        ((Graphics2D)g).setStroke(new BasicStroke(borderWidth));
        g.drawPolygon(makeCross());
    }


    @Override
    public boolean contains(int x, int y) {
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        int radius = (int) Math.sqrt(getWidth()*getWidth()/4.0 + getHeight()*getHeight()/4.0);
        int dx = x - centerX;
        int dy = y - centerY;
        return dx * dx + dy * dy <= radius * radius;
    }

    private Polygon makeCross(){
        int centerX = getWidth()/2;
        int centerY = getHeight()/2;
        int[] xPoints = new int[]{0, centerX, getWidth(), centerX, getWidth(), centerX, 0, centerX};
        int[] yPoints = new int[]{0, centerY, getHeight(), centerY, 0, centerY, getHeight(), centerY};
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    public void setSize(int size) {
        setPreferredSize(new Dimension(size, size));
        setSize(new Dimension(size, size));
    }

    public void setCrossWidth(int width) {
        borderWidth = width;
        setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(
                borderWidth, borderWidth, borderWidth, borderWidth),
                getBorder()));
    }
}
