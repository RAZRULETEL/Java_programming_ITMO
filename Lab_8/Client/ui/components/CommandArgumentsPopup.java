package Client.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;

import Client.ui.Toast;
import Client.ui.components.texts.JTextFieldBeautiful;
import Client.ui.components.texts.JTextView;
import Client.ui.localization.UIField;
import Shared.command_processing.ResultDTO;
import Shared.commands.interfaces.Command;

public class CommandArgumentsPopup extends JDialog {
    private static final Color ERROR_COLOR = Color.RED;
    private static final int ARG_FIELD_COLUMNS = 16, FONT_SIZE = 16, BORDER_RADIUS = 25, MIN_WIDTH = ARG_FIELD_COLUMNS*18, MIN_HEIGHT = 250, MARGIN_TOP = 50;
    private Command command;
    public CommandArgumentsPopup(Command command) {
        this(command, 0, MARGIN_TOP);
    }

    public CommandArgumentsPopup(Command command, JComponent component) {
        this(command, component.getLocationOnScreen().x + component.getWidth() / 2, component.getLocationOnScreen().y + component.getHeight() / 2);
    }

    public CommandArgumentsPopup(Command command, int x, int y){
        super((java.awt.Frame) null, command.getClass().getSimpleName(), true);
        setLocation(x, Math.max(y, MARGIN_TOP));
        this.command = command;
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridBagLayout());
        add(contentPanel);
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.BOTH;


        JTextFieldBeautiful[] args = new JTextFieldBeautiful[command.getArgs().length];
        for(int i = 0; i < command.getArgs().length; i++){
            JPanel line = new JPanel();
            JTextView text = new JTextView(LanguageSelect.getCurrentLocale().getString(command.getArgs()[i].toString()));
            line.add(text);
            args[i] = new JTextFieldBeautiful("");
            args[i].setColumns(ARG_FIELD_COLUMNS);
            args[i].setFontSize(FONT_SIZE);
            args[i].setBorderRadius(BORDER_RADIUS);
            line.add(args[i]);
            contentPanel.add(line, c);
        }
        c.fill = GridBagConstraints.HORIZONTAL;
        JButton button = new JButton(LanguageSelect.getCurrentLocale().getString(UIField.Execute.toString()));
        contentPanel.add(button, c);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String[] stringArray = new String[args.length];
                for (int i = 0; i < args.length; i++) {
                    stringArray[i] = args[i].getText();
                }
                ResultDTO validationResult = command.validate(stringArray);
                if(!validationResult.getSuccess()) {
                    new Toast(validationResult.getFormattedString(), contentPanel, ERROR_COLOR).start();
                    return;
                }
                CommandArgumentsPopup.this.command = command;
                dispose();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                CommandArgumentsPopup.this.command = null;
                dispose();
            }
        });
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));
        setVisible(true);
    }

    public Command getValidatedCommand(){
        return command;
    }

}
