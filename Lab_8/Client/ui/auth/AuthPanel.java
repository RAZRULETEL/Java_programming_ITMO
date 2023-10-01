package Client.ui.auth;

import static java.awt.GridBagConstraints.CENTER;
import static java.awt.GridBagConstraints.REMAINDER;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.HashMap;
import java.util.ListResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import Client.Main;
import Client.client_commands.LoginSender;
import Client.client_commands.RegisterSender;
import Client.command_processing.ClientCommandProcessor;
import Client.ui.AuthPanelManager;
import Client.ui.Toast;
import Client.ui.components.LanguageSelect;
import Client.ui.components.Localizer;
import Client.ui.components.buttons.JRhombusButton;
import Client.ui.components.texts.JTextFieldBeautiful;
import Client.ui.components.texts.JTextView;
import Client.ui.interfaces.LocalizedPanel;
import Client.ui.localization.UIField;
import Client.ui.table.TablePanel;
import Shared.commands.enums.CommandMessage;

public class AuthPanel extends JPanel implements LocalizedPanel {
    private static final int INPUT_FIELDS_WIDTH = 14, INPUT_FIELDS_FONT_SIZE = 22, INPUT_FIELDS_BORDER_RADIUS = 40, INPUT_FIELDS_PADDING = 4, BUTTON_BORDER_WIDTH = 3, BUTTON_WIDTH = 300, BUTTON_HEIGHT = 150, MIN_WIDTH = 800, MIN_HEIGHT = 500;
    private static final Color BUTTON_BORDER = Color.GREEN;
    private static final char PasswordVisualReplacement = '*';
    private final HashMap<JComponent, UIField> localizedComponents = new HashMap<>();
    private final LanguageSelect languageSelector;

    public AuthPanel() {
        setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.EAST;
        constraints.gridwidth = REMAINDER;

        languageSelector = new LanguageSelect();
        add(languageSelector, constraints);

        constraints.anchor = CENTER;//Align center
        constraints.fill = GridBagConstraints.BOTH;//Stretch by height and width
        constraints.weighty = 1;
        JTextView title = new JTextView();
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFontSize(TablePanel.TITLE_FONT_SIZE);
        title.setText(getCurrentLocale().getString(UIField.AuthTitle.toString()));
        add(title, constraints);
        localizedComponents.put(title, UIField.AuthTitle);

        constraints.fill = GridBagConstraints.NONE;
        JTextFieldBeautiful login = new JTextFieldBeautiful(getCurrentLocale().getString(UIField.Login.toString()));
        Border loginBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING),
                login.getBorder()
        );
        login.setBorderRadius(INPUT_FIELDS_BORDER_RADIUS);
        login.setColumns(INPUT_FIELDS_WIDTH);
        login.setFontSize(INPUT_FIELDS_FONT_SIZE);
        login.setBorder(loginBorder);
        add(login, constraints);
        localizedComponents.put(login, UIField.Login);

        JTextFieldBeautiful password = new JTextFieldBeautiful(getCurrentLocale().getString(UIField.Password.toString()));
        Border passwordBorder = BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING, INPUT_FIELDS_PADDING),
                password.getBorder()
        );
        password.setEchoChar(PasswordVisualReplacement);
        password.setBorder(passwordBorder);
        password.setBorderRadius(INPUT_FIELDS_BORDER_RADIUS);
        password.setColumns(INPUT_FIELDS_WIDTH);
        password.setFontSize(INPUT_FIELDS_FONT_SIZE);
        add(password, constraints);
        localizedComponents.put(password, UIField.Password);

        constraints.gridx = 0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        JRhombusButton register = new JRhombusButton(getCurrentLocale().getString(UIField.Register.toString()), new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        register.setFontSize(INPUT_FIELDS_FONT_SIZE);
        register.setBackground(Main.TRANSPARENT);
        register.setBorderColor(BUTTON_BORDER);
        register.setBorderWidth(BUTTON_BORDER_WIDTH);
        add(register, constraints);
        register.addActionListener(new ActionListener() {
            private final RegisterSender regSender = new RegisterSender();
            private final ClientCommandProcessor processor = new ClientCommandProcessor();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommandMessage res = regSender.validateLocaled(new String[]{login.getText(), password.getText()});
                        if (res.isPositive())
                            res = processor.processLocalizedLocalCommand(regSender);
                        new Toast(res.getFormattedMessage(getCurrentLocale()), getLocationOnScreen().x + getWidth() / 2, (int) (getLocationOnScreen().y + getHeight() * 0.8), res.isPositive() ? Color.GREEN : Color.RED).start();
                        if (res.isPositive())
                            ((AuthPanelManager) getParent()).showNext();
                    }
                }).start();
            }
        });
        localizedComponents.put(register, UIField.Register);

        constraints.gridx = 1;
        constraints.gridwidth = REMAINDER;
        JRhombusButton authorize = new JRhombusButton(getCurrentLocale().getString(UIField.Authorize.toString()), new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        authorize.setFontSize(INPUT_FIELDS_FONT_SIZE);
        authorize.setBackground(Main.TRANSPARENT);
        authorize.setBorderColor(BUTTON_BORDER);
        authorize.setBorderWidth(BUTTON_BORDER_WIDTH);
        add(authorize, constraints);
        authorize.addActionListener(new ActionListener() {
            private final LoginSender loginSender = new LoginSender();
            private final ClientCommandProcessor processor = new ClientCommandProcessor();

            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        CommandMessage res = loginSender.validateLocaled(new String[]{login.getText(), password.getText()});
                        if (res.isPositive())
                            res = processor.processLocalizedLocalCommand(loginSender);
                        new Toast(res.getFormattedMessage(getCurrentLocale()), getLocationOnScreen().x + getWidth() / 2, (int) (getLocationOnScreen().y + getHeight() * 0.8), res.isPositive() ? Color.GREEN : Color.RED).start();
                        if (res.isPositive())
                            ((AuthPanelManager) getParent()).showNext();
                    }
                }).start();
            }
        });
        localizedComponents.put(authorize, UIField.Authorize);

        languageSelector.addLocaleChangeListener(new Localizer(localizedComponents));

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SwingUtilities.getRoot(AuthPanel.this).setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                SwingUtilities.getRoot(AuthPanel.this).setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
                setLocale(LanguageSelect.getCurrentLocale());
            }
        });
    }

    @Override
    public void setLocale(ListResourceBundle locale) {
        if (locale == null)
            throw new NullPointerException("Locale cannot be null");
        languageSelector.setCurrentLocale(locale);
    }

    @Override
    public ListResourceBundle getCurrentLocale() {
        return languageSelector.getCurrentLocale();
    }
}
