package Client.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Client.command_processing.ClientCommandProcessor;
import Client.ui.components.texts.JTextView;

public class UserLoginView extends JPanel {
    private final static int DEFAULT_ICON_SIZE = 20;
    private final static int FONT_SIZE = 18;

    private JTextView login;

    public UserLoginView(){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("user.png");
        if(resource != null) {
            ImageIcon icon = new ImageIcon(resource);
            Image scaledImage = icon.getImage().getScaledInstance(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE, Image.SCALE_AREA_AVERAGING);
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            label.setPreferredSize(new Dimension(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE));
            add(label);
        }
        login = new JTextView();
        login.setFocusable(false);
        login.setFontSize(FONT_SIZE);
        login.setBackground(Color.RED);
        add(login);
//        setBackground(Color.CYAN);
    }

    public void updateLogin(){
        login.setText(ClientCommandProcessor.getLogin());
    }
}
