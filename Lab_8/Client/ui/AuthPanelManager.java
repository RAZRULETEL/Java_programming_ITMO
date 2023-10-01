package Client.ui;

import java.awt.CardLayout;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class AuthPanelManager extends JPanel {

    private static final String AUTH_PANEL = "authPanel";
    private final String[] panels;
    private JComponent[] panelObjects;
    private final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    private int currentPanel = 0;

    public AuthPanelManager(JComponent authPanel, JComponent... tabs) {
        setLayout(new CardLayout());
        panelObjects = new JComponent[tabs.length + 1];
        panelObjects[0] = authPanel;
        panels = new String[tabs.length + 1];
        panels[0] = AUTH_PANEL;
        add(authPanel, AUTH_PANEL);
        for (int i = 1; i < tabs.length + 1; i++) {
            panelObjects[i] = tabs[i - 1];
            panels[i] = "panel" + i;
            add(tabs[i - 1], panels[i]);
        }
        ((CardLayout) getLayout()).show(this, AUTH_PANEL);
    }

    public void showAuthPanel() {
        currentPanel = 0;
        ((CardLayout) getLayout()).show(this, AUTH_PANEL);
    }

    public void showNext() {
        if (currentPanel == panels.length - 1)
            currentPanel = 1;//Skipping auth panel
        else
            currentPanel++;
        ((CardLayout) getLayout()).show(this, panels[currentPanel]);
    }

    public boolean isShowingAuthPanel() {
        return panels[currentPanel].equals(AUTH_PANEL);
    }

    public void addTask(Runnable run){
        singleThreadExecutor.execute(run);
    }
}
