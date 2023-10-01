package Client;

import java.awt.Color;
import java.net.SocketException;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import Client.network.NetworkTools;
import Client.ui.AuthPanelManager;
import Client.ui.auth.AuthPanel;
import Client.ui.graph.VisualisationPanel;
import Client.ui.table.TablePanel;

public class Main {
    private static final Locale[] locales =  new Locale[]{new Locale("ru"), new Locale("sk"), new Locale("el"), new Locale("es", "DO")};
    public static final Color TRANSPARENT = new Color(0x0000000, true);
    private static int port = 4145;

    public static void main(String[] args) {
        if(System.getenv().containsKey("PORT"))
            port = Integer.parseInt(System.getenv("PORT"));
        else
            System.out.println("Переменная окружения PORT не обнаружена");

        NetworkTools network;
        try {
            network = NetworkTools.getInstance("localhost", port);
            NetworkTools finalNetwork = network;
            SwingUtilities.invokeLater(()->{
                gui(finalNetwork);
            });
        }catch (IllegalArgumentException|SocketException e){
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    private static void gui(NetworkTools network){
        JFrame main = new JFrame();
        main.add(new AuthPanelManager(new AuthPanel(), new TablePanel(network), new VisualisationPanel(network)));
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setSize(1200,600);
        main.setVisible(true);
    }

}
