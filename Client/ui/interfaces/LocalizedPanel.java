package Client.ui.interfaces;

import java.util.ListResourceBundle;

public interface LocalizedPanel {
    void setLocale(ListResourceBundle locale);
    ListResourceBundle getCurrentLocale();
}
