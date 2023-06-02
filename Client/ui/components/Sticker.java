package Client.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;

import javax.swing.JPanel;

import Client.ui.components.buttons.CrossButton;
import Client.ui.components.texts.JTextView;

public class Sticker extends JPanel {
    private static final int BORDER_RADIUS = 5;
    private static final Color[] StickerColors = new Color[]{Color.RED, Color.GREEN, Color.PINK, Color.YELLOW, Color.LIGHT_GRAY};
    private final JTextView stickerText;
    private final CrossButton removeSticker;

    public Sticker(String text){
        this(text, StickerColors[new Random().nextInt(StickerColors.length)]);
    }

    public Sticker(String text, Color color) {
        stickerText = new JTextView(text);
        add(stickerText);
        removeSticker = new CrossButton(stickerText.getFontSize());
        removeSticker.setBackground(Color.BLACK);
        removeSticker.setCrossWidth(3);
        add(removeSticker);
        setBackground(color);
        removeSticker.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, BORDER_RADIUS, BORDER_RADIUS);
    }

    @Override
    protected void paintBorder(Graphics g) {}

    public void setFontSize(int size) {
        stickerText.setFontSize(size);
        removeSticker.setSize(size);
    }

    public JTextView getTextView(){
        return stickerText;
    }

    public void addCloseButtonListener(MouseListener listener){
        removeSticker.addMouseListener(listener);
    }

    public void close(){
        Sticker.this.setVisible(false);
        Sticker.this.removeAll();
    }
}
