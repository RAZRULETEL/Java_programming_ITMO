package Client.ui.components;

import static java.util.Locale.getAvailableLocales;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

import Client.ui.interfaces.LocaleListener;

public class LanguageSelect extends JPanel {
    private static final String LocaleBundleName = "Client.ui.localization.loc";
    private static ListResourceBundle[] locales;
    private final static int DEFAULT_ICON_SIZE = 32;
    private final static int FONT_SIZE = 16;
    private static ListResourceBundle currentLocale;
    private ArrayList<LocaleListener> listeners = new ArrayList<>();
    private JComboBox<ListResourceBundle> localesList;

    static {
        Locale[] locales = getAvailableLocales();
        HashSet<ListResourceBundle> bundles = new HashSet<>();
        for(Locale locale : locales)
            try{
                bundles.add((ListResourceBundle) ResourceBundle.getBundle(LocaleBundleName, locale));
            }catch (MissingResourceException ignored){
            }
        LanguageSelect.locales = bundles.stream().sorted((b1, b2) -> b2.toString().compareTo(b1.toString())).toList().toArray(ListResourceBundle[]::new);
        currentLocale = LanguageSelect.locales[0];
    }

    public LanguageSelect(){
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource("world.png");
        if(resource != null) {
            ImageIcon icon = new ImageIcon(resource);
            Image scaledImage = icon.getImage().getScaledInstance(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE, Image.SCALE_AREA_AVERAGING);
            JLabel label = new JLabel(new ImageIcon(scaledImage));
            label.setPreferredSize(new Dimension(DEFAULT_ICON_SIZE, DEFAULT_ICON_SIZE));
            add(label);
        }


        localesList = new JComboBox<>(locales);
        localesList.setSelectedItem(currentLocale == null ? locales[0] : currentLocale);
        localesList.setFont(localesList.getFont().deriveFont((float)FONT_SIZE));
        localesList.setBorder(new LineBorder(Color.BLACK));
        localesList.setBackground(Color.WHITE);
        localesList.setFocusable(false);
        localesList.setUI(new BasicComboBoxUI() {
            @Override
            protected JButton createArrowButton() {
                JButton arrowButton = new BasicArrowButton(BasicArrowButton.SOUTH);
                arrowButton.setBackground(comboBox.getBackground());
                return arrowButton;
            }
        });

        localesList.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                currentLocale = (ListResourceBundle) itemEvent.getItem();
                for(LocaleListener listener : listeners)
                    listener.onLocaleChanged((ListResourceBundle) itemEvent.getItem());
            }
        });
        add(localesList);
    }

    public void setCurrentLocale(ListResourceBundle locale){
        localesList.setSelectedItem(locale);
        currentLocale = locale;
        for(LocaleListener listener : listeners)
            listener.onLocaleChanged(locale);
    }
    public void addLocaleChangeListener(LocaleListener listener){
        listeners.add(listener);
    }
    public static ListResourceBundle getCurrentLocale(){
        return currentLocale;
    }
}