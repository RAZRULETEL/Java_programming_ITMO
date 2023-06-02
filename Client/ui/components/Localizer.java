package Client.ui.components;

import java.util.HashMap;
import java.util.ListResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import Client.ui.components.texts.JTextFieldBeautiful;
import Client.ui.interfaces.LocaleListener;
import Client.ui.localization.UIField;

public class Localizer implements LocaleListener {
    private final HashMap<JComponent, UIField> components;

    public Localizer(HashMap<JComponent, UIField> localizedComponents){
        components = localizedComponents;
    }

    @Override
    public void onLocaleChanged(ListResourceBundle newLocale) {
        for(JComponent component : components.keySet())
            if(component instanceof JTextFieldBeautiful)
                ((JTextFieldBeautiful)component).setHint(newLocale.getString(components.get(component).toString()));
            else if(component instanceof JTextComponent)
                ((JTextComponent)component).setText(newLocale.getString(components.get(component).toString()));
            else if(component instanceof JLabel)
                ((JLabel)component).setText(newLocale.getString(components.get(component).toString()));
            else
                ((JButton)component).setText(newLocale.getString(components.get(component).toString()));

    }
}
