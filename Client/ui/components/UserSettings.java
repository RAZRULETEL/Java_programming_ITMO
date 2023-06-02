package Client.ui.components;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ListResourceBundle;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Client.command_processing.ClientCommandProcessor;
import Client.ui.components.texts.JTextView;
import Client.ui.interfaces.LocaleListener;
import Client.ui.localization.UIField;

public class UserSettings extends JDialog {
    private final static int FONT_SIZE = 18, MIN_WIDTH = 400, MIN_HEIGHT = 200, ELEMENTS_MARGIN = 5;

    public UserSettings(LocaleListener localeListener){
        super((java.awt.Frame) null, true);
        setContentPane(new JPanel());
        getContentPane().setLayout(new GridBagLayout());
        setMinimumSize(new Dimension(MIN_WIDTH,MIN_HEIGHT));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        GridBagConstraints c = new GridBagConstraints();
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(ELEMENTS_MARGIN,ELEMENTS_MARGIN,ELEMENTS_MARGIN,ELEMENTS_MARGIN);
        c.weighty = 1;
        c.anchor = GridBagConstraints.NORTH;
        LanguageSelect languageSelect = new LanguageSelect();

        JTextView title = new JTextView(languageSelect.getCurrentLocale().getString(UIField.LoggedAs.toString())+" "+ClientCommandProcessor.getLogin());
        title.setFocusable(false);
        title.setFontSize(FONT_SIZE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, c);


        c.anchor = GridBagConstraints.CENTER;
        add(languageSelect, c);


        c.anchor = GridBagConstraints.SOUTH;
        JButton logout = new JButton(languageSelect.getCurrentLocale().getString(UIField.Logout.toString()));
        add(logout, c);
        logout.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ClientCommandProcessor.removeCredentials();
                dispose();
            }
        });
        languageSelect.addLocaleChangeListener(localeListener);
        languageSelect.addLocaleChangeListener(new LocaleListener() {
            @Override
            public void onLocaleChanged(ListResourceBundle newLocale) {
                title.setText(newLocale.getString(UIField.LoggedAs.toString())+" "+ClientCommandProcessor.getLogin());
                logout.setText(newLocale.getString(UIField.Logout.toString()));
            }
        });
    }
}
