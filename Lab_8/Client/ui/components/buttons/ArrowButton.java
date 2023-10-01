package Client.ui.components.buttons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import Client.Main;

public class ArrowButton extends JButton {
    private int borderWidth = 1;
    private Dimension paddings;
    private Color borderColor = Color.BLACK;
    private Border border;
    private float arrowWidth, arrowHeight;
    private Insets textPadding = new Insets(0,0,0,0);

    public ArrowButton(String text, float arrowLength, float arrowHeight) {
        super(text);
        setOpaque(false);
        arrowWidth = arrowLength;
        this.arrowHeight = arrowHeight;
        updatePaddings();
        setBackground(Main.TRANSPARENT);
    }
    {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                updatePaddings();
                setBorder(border);
            }
        });
        setFocusable(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillPolygon(getArrow());
        super.paintComponent(g);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderWidth(int width) {
        if(width < 0)
            throw new IllegalArgumentException("Width can't be negative");
        borderWidth = width;
    }

    @Override
    protected void paintBorder(Graphics g) {
        g.setColor(borderColor);
        ((Graphics2D)g).setStroke(new BasicStroke(borderWidth));
        g.drawPolygon(getArrow());
    }

    @Override
    public void setBorder(Border border){
        if(paddings == null)
            return;
        this.border = border;
        Border paddedBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(paddings.height, paddings.width, paddings.height, paddings.width),
                border
        );
        super.setBorder(paddedBorder);
    }

    @Override
    public boolean contains(int x, int y) {
        return getArrow().contains(x, y);
    }

    @Override
    public void setText(String text){
        super.setText(text);
        updatePaddings();
        setBorder(border);
    }

    public void setFontSize(float fontSize) {
        setFont(getFont().deriveFont(fontSize));
    }

    private Polygon getArrow(){
        int[] xPoints;
        int[] yPoints;
            int noBorderWidth = getWidth() - paddings.width*2 - borderWidth+1;
            int noBorderHeight = getHeight() - paddings.height*2 - borderWidth;
            int topBorder = textPadding == null ? 0 : -textPadding.top;
            int bottomBorder = noBorderHeight + (textPadding == null ? 0 : textPadding.bottom);
            int leftBorder = textPadding == null ? 0 : -textPadding.left;
            int rightBorder = noBorderWidth + (textPadding == null ? 0 : textPadding.right);
            xPoints = new int[]{leftBorder, rightBorder, rightBorder, leftBorder, leftBorder, (int) -(rightBorder*arrowWidth), leftBorder, leftBorder, leftBorder};
            yPoints = new int[]{topBorder, topBorder, bottomBorder, bottomBorder, (int) (bottomBorder*arrowHeight), bottomBorder/2, (int) -(bottomBorder*(arrowHeight-1)), topBorder, bottomBorder};
            for(int i = 0; i < xPoints.length; i++) {
                xPoints[i] += (getWidth()-noBorderWidth)/2;
                yPoints[i] += (getHeight()-noBorderHeight)/2;
            }
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    public void updatePaddings(){
        if(paddings == null)
            paddings = new Dimension(0,0);
        if(textPadding == null)
            textPadding = new Insets(0,0,0,0);
        int noBorderWidth = getWidth() - paddings.width*2 - borderWidth;
        int noBorderHeight = getHeight() - paddings.height*2 - borderWidth;
        int newLeftPadding = (int) Math.max(noBorderWidth*arrowWidth+1+Math.max(textPadding.left, textPadding.right), 0);
        int newVertPadding = (int) Math.max(noBorderHeight*(arrowHeight-1)+1+Math.max(textPadding.top, textPadding.bottom),0);
        paddings = new Dimension(newLeftPadding, newVertPadding);
    }

    public void setTextPadding(Insets padding){
        textPadding = padding;
    }
}
