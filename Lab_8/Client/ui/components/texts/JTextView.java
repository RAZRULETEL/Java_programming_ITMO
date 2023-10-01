package Client.ui.components.texts;

import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class JTextView extends JLabel {
    private Border border;

    private Insets paddings;

    public JTextView(){
        this(null);
    }
    public JTextView(String text) {
        super(text);
    }

    public void setFontSize(float fontSize) {
        setFont(getFont().deriveFont(fontSize));
    }

    public int getFontSize() {return getFont().getSize();}

    @Override
    public Border getBorder() {
        return border;
    }

    @Override
    public void setBorder(Border border) {
        this.border = border;
        super.setBorder(new CompoundBorder(border, new EmptyBorder(paddings)));
    }

    public void setPaddings(Insets paddings){
        this.paddings = paddings;
        setBorder(new CompoundBorder(border, new EmptyBorder(paddings)));
    }

    public void setPaddings(int horizontalPadding, int verticalPadding){
        paddings = new Insets(verticalPadding, horizontalPadding, verticalPadding, horizontalPadding);
        setBorder(new CompoundBorder(border, new EmptyBorder(paddings)));
    }
}
