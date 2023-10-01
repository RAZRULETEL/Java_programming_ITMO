package Client.ui.components.buttons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.Border;

import Client.ui.animators.Animator;
import Client.ui.animators.PropellerAnimator;
import Client.ui.animators.ScaleAnimator;

public class JRhombusButton extends JButton {
    private static final int LINE_INTERVAL = 3, ANIMATION_INTERVAL = 20;
    private static final float PRESSED_SCALE = 1.1f;
    private int width, height, borderWidth = 1;
    private Dimension paddings;
    private Color borderColor = Color.BLACK;
    private Border border;
    private String text;
    private final CopyOnWriteArrayList<Animator> animators = new CopyOnWriteArrayList<>();

    public JRhombusButton(String text, int size) {
        this(text, new Dimension(size, size));
    }

    public JRhombusButton(String text, Dimension size) {
        super();
        this.text = text;
        setOpaque(false);
        width = size.width;
        height = size.height;
        setFocusable(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updatePaddings();
                setBorder(border);
            }
        });
        PropellerAnimator propAnim = new PropellerAnimator(this, ANIMATION_INTERVAL);
        animators.add(propAnim);
        propAnim.setRepeats(true);
        animators.add(new ScaleAnimator(this, PRESSED_SCALE));
    }

    @Override//Making JButton multiline
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Drawing background
        g2d.setColor(getBackground());
        g2d.fillPolygon(getRhombus());

        for(Animator anim : animators)
            anim.animate(g2d);

        g2d.setFont(getFont());
        g2d.setColor(getForeground());

        FontRenderContext frc = g2d.getFontRenderContext();

        int width = getWidth();
        int height = getHeight();

        String[] lines = text.split("\\n");
        int lineHeight = (int) new TextLayout(text, getFont(), frc).getBounds().getHeight() + LINE_INTERVAL;
        int startY = (int) ((height - lineHeight - lines.length * lineHeight / 2) / 2);//Formula picked randomly to look nice
        for (String line : lines) {
            line = line.trim();
            TextLayout layout = new TextLayout(line, getFont(), frc);//Creates layout to get sizes of text that will drawn
            Rectangle2D bounds = layout.getBounds();
            int startX = (width - (int) bounds.getWidth()) / 2;//Align text center
            g2d.drawString(line, startX, startY + lineHeight);
            startY += lineHeight;
        }
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public void setBorderWidth(int width) {
        if(width < 0)
            throw new IllegalArgumentException("Width can't be negative");
        borderWidth = width;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    @Override
    protected void paintBorder(Graphics g) {
        for(Animator anim : animators)
            anim.animateBorder((Graphics2D)g);

        g.setColor(borderColor);
        ((Graphics2D)g).setStroke(new BasicStroke(borderWidth));
        g.drawPolygon(getRhombus());
    }

    @Override
    public void setBorder(Border border){
        this.border = border;
        Border paddedBorder = border;
        if(paddings != null)
            paddedBorder = BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(paddings.height, paddings.width, paddings.height, paddings.width),
                    border
            );
        super.setBorder(paddedBorder);
    }

    @Override
    public boolean contains(int x, int y) {
        return getRhombus().contains(x, y);
    }

    @Override
    public void setText(String text){
        this.text = text;
        updatePaddings();
        setBorder(border);
    }

    @Override
    public String getText(){
        return text;
    }

    public void setFontSize(float fontSize) {
        setFont(getFont().deriveFont(fontSize));
    }

    private Polygon getRhombus(){
        int[] xPoints;
        int[] yPoints;
        xPoints = new int[]{(getWidth() - width + borderWidth)/2, getWidth() / 2, (getWidth() + width - borderWidth)/2, getWidth() / 2};
        yPoints = new int[]{getHeight() / 2, (getHeight() + height - borderWidth)/2, getHeight() / 2, (getHeight() - height + borderWidth)/2};
        return new Polygon(xPoints, yPoints, 4);
    }

    public void updatePaddings(){
        if(paddings == null)
            paddings = new Dimension(0,0);
        if(width != 0 && height != 0) {
            int noBorderWidth = getWidth() - paddings.width*2 - borderWidth;
            int noBorderHeight = getHeight() - paddings.height*2 - borderWidth;
            int newHorPadding = Math.max((width > noBorderWidth ? width - noBorderWidth : noBorderWidth - width) / 2 + 1 + (borderWidth - 1), 0);
            int newVertPadding = Math.max((height > noBorderHeight ? height - noBorderHeight : noBorderHeight - height) / 2 + 1 + (borderWidth - 1),0);
            paddings = new Dimension(newHorPadding, newVertPadding);
        }
    }

    public void setBounds(int x, int y){
        super.setBounds(x, y, width, height);
    }

    public void addAnimator(Animator animator){
        if(animator == null)
            throw new NullPointerException("Animator cannot be null");
        animators.add(animator);
    }

    public List<Animator> getAnimators(){
        return animators;
    }

    public void clearAnimators(){
        animators.clear();
    }

    public void removeAnimator(Animator animator){
        animators.remove(animator);
    }
}
