package Client.ui.graph;

import static java.awt.GridBagConstraints.REMAINDER;
import static Client.ui.table.TablePanel.BUTTON_BORDER_WIDTH;
import static Client.ui.table.TablePanel.MID_BUTTON_SIZE;
import static Client.ui.table.TablePanel.SMALL_BUTTON_SIZE;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import Client.Main;
import Client.command_processing.AsyncClientCommandProcessor;
import Client.command_processing.ClientCommandProcessor;
import Client.network.NetworkTools;
import Client.network.PacketListener;
import Client.ui.AuthPanelManager;
import Client.ui.Toast;
import Client.ui.components.CommandArgumentsPopup;
import Client.ui.components.LanguageSelect;
import Client.ui.components.Localizer;
import Client.ui.components.UserLoginView;
import Client.ui.components.UserSettings;
import Client.ui.components.buttons.ArrowButton;
import Client.ui.components.buttons.JRhombusButton;
import Client.ui.components.texts.JTextView;
import Client.ui.interfaces.LocalizedPanel;
import Client.ui.localization.UIField;
import Client.ui.table.CollectionTableModel;
import Client.ui.table.TablePanel;
import Shared.command_processing.ArrayDTO;
import Shared.command_processing.ResultDTO;
import Shared.commands.Clear;
import Shared.commands.Insert;
import Shared.commands.RemoveByKey;
import Shared.network.PacketType;
import Shared.resources.Route;

public class VisualisationPanel extends JPanel implements LocalizedPanel {
    private static final int VISUALIZATION_WIDTH = 500, VISUALIZATION_HEIGHT = 500, FONT_SIZE = TablePanel.FONT_SIZE, VISUALIZATION_PADDING = 5, FRAMES_PER_SECOND = 30, MAX_PLANET_RADIUS = 50, MIN_PLANETS_CONTROL_WIDTH = 150, TABLE_HEIGHT = 30, MINIMAL_TOUCHABLE_RADIUS = 5, MIN_PANEL_WIDTH = VISUALIZATION_WIDTH+VISUALIZATION_PADDING*2+MIN_PLANETS_CONTROL_WIDTH+SMALL_BUTTON_SIZE + 20;
    private final HashMap<JComponent, UIField> localizedComponents = new HashMap<>();
    private ListResourceBundle currentLocale;
    private HashMap<Integer, Route> collection;
    private HashMap<String, ArrayList<Integer>> usersObjects;
    private HashMap<String, Color> userColors = new HashMap<>();
    private CopyOnWriteArrayList<Planet> planets = new CopyOnWriteArrayList<>();
    private CopyOnWriteArrayList<Planet> planetsCheckpoint = new CopyOnWriteArrayList<>();
    private boolean simulating = false;
    private PlanetControls planetControls;
    private final AsyncClientCommandProcessor processor;
    private long lastSyncTime, simulationStartTime;
    private final JTable objInfo;
    private JPanel simulatingPanel;
    private JButton startButton;
    private final Runnable receiveTask;

    public VisualisationPanel(NetworkTools network) {
        currentLocale = LanguageSelect.getCurrentLocale();
        processor = new AsyncClientCommandProcessor(currentLocale);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                setLocale(LanguageSelect.getCurrentLocale());
//                SwingUtilities.getRoot(VisualisationPanel.this).setMinimumSize(new Dimension((int) (MIN_PANEL_WIDTH*TablePanel.MIN_SIZE_COEFFICIENT), (int) ((VISUALIZATION_HEIGHT+VISUALIZATION_PADDING*2+TablePanel.TOP_LINE_HEIGHT + TABLE_HEIGHT)*TablePanel.MIN_SIZE_COEFFICIENT)));
            }
        });



        network.addPacketListener(PacketType.FullCollection, new PacketListener() {
            @Override
            public void onPacketReceived(ResultDTO packet) {
                if (packet.getSuccess()) {
                    if (packet.getSuccess() && packet instanceof ArrayDTO && ((ArrayDTO) packet).getData().length > 0 && ((ArrayDTO) packet).getData()[0] instanceof HashMap)
                        try {
                            collection = (HashMap<Integer, Route>) ((ArrayDTO) packet).getData()[0];
                            usersObjects = (HashMap<String, ArrayList<Integer>>) ((ArrayDTO) packet).getData()[1];
                        } catch (ClassCastException ignored) {}
                    else
                        new Toast(packet.getFormattedString(), VisualisationPanel.this).start();
                    for (Route obj : collection.values()) {
                        String user = "";
                        Iterator<String> users = usersObjects.keySet().iterator();
                        while (users.hasNext() && (usersObjects.get(user) == null || !usersObjects.get(user).contains(obj.getId())))
                            user = users.next();
                        if (userColors.get(user) == null)
                            userColors.put(user, getRandomColor());
                        if(planets.stream().noneMatch(planet -> planet.getRoute().getId() == obj.getId())) {
                            planets.add(new Planet(obj, userColors.get(user), VISUALIZATION_PADDING, VISUALIZATION_PADDING, VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT, MAX_PLANET_RADIUS));
                            planets.get(planets.size() - 1).addOnChangeListener(new PlanetSynchronizer(processor));
                        }else{
                            int l = 0;
                            while(l < planets.size() && obj.getId() != planets.get(l).getRoute().getId())
                                l++;
                            if(l < planets.size())
                                planets.get(l).updateByRoute(obj);
                        }
                    }
                }
            }
        });
        network.addPacketListener(PacketType.InsertCollectionObject, new PacketListener() {
            @Override
            public void onPacketReceived(ResultDTO packet) {
                if (packet instanceof ArrayDTO) {
                    lastSyncTime = System.currentTimeMillis();
                    int key = -1, route = -1, userIndx = -1;
                    ArrayList<Object> objs = new ArrayList<>(List.of(((ArrayDTO) packet).getData()));
                    for (int i = 0; i < objs.size(); i++) {
                        if (objs.get(i) instanceof String)
                            userIndx = i;
                        if (objs.get(i) instanceof Route)
                            route = i;
                        if (objs.get(i) instanceof Integer)
                            key = i;
                    }
                    if (userIndx != -1 && key != -1 && route != -1 && collection != null) {
                        Route rt = (Route) objs.get(route);
                        String user = (String) objs.get(userIndx);
                        collection.put((Integer) objs.get(key), rt);
                        usersObjects.computeIfAbsent(user, k -> new ArrayList<>());
                        usersObjects.get(user).add(rt.getId());
                        if (!planets.stream().map(planet -> planet.getRoute().getId()).toList().contains(rt.getId())) {
                            if (userColors.get(user) == null)
                                userColors.put(user, getRandomColor());
                            planets.add(new Planet(rt, userColors.get(user), VISUALIZATION_PADDING, VISUALIZATION_PADDING, VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT, MAX_PLANET_RADIUS));
                            planets.get(planets.size() - 1).addOnChangeListener(new PlanetSynchronizer(processor));
                        } else {
                            if (!simulating)
                                for (Planet planet : planets)
                                    if (planet.getRoute().getId() == rt.getId()) {
                                        planet.updateByRoute(rt);
                                    }
                        }
                    }
                } else if (VisualisationPanel.this.isShowing())
                    new Toast(packet.getFormattedString(), VisualisationPanel.this, packet.getSuccess() ? Color.GREEN : Color.RED).start();
            }
        });
        network.addPacketListener(PacketType.ClearCollection, new PacketListener() {
            @Override
            public void onPacketReceived(ResultDTO packet) {
                if (packet instanceof ArrayDTO) {
                    Object[] ids = ((ArrayDTO) packet).getData();
                    for (Object id : ids)
                        if (id.getClass() == Integer.class) {
                            planets.removeIf(planet -> planet.getRoute().getId() == ((Integer) id));
                        }
                }
            }
        });



        objInfo = new JTable(new CollectionTableModel());
        ((CollectionTableModel)objInfo.getModel()).setDataFromCollection(TablePanel.Columns, new int[0], new Route[0], currentLocale);
        JScrollPane tablePane = new JScrollPane(objInfo);
        tablePane.setPreferredSize(new Dimension(Integer.MAX_VALUE, (int) (TABLE_HEIGHT*TablePanel.MIN_SIZE_COEFFICIENT)));



        add(makeTopLine());
        add(makeVisualisationLine());
        add(tablePane);



        new Thread(new Runnable() {

            @Override
            public void run() {
                int frameTime = 1000/FRAMES_PER_SECOND;
                while (true) {
                    simulatingPanel.updateUI();
                    if(System.currentTimeMillis() - lastSyncTime < frameTime * 5) {
                        if(!simulating) {
                            startButton.setEnabled(false);
                            startButton.setText(currentLocale.getString(UIField.Start.toString()));
                            planetControls.setEnabled(false);
                        }
                    }else {
                        startButton.setEnabled(true);
                        planetControls.setEnabled(true);
                        if(simulating && System.currentTimeMillis() - simulationStartTime > frameTime * 5) {
                            startButton.setText(currentLocale.getString(UIField.Start.toString()));
                            simulating = false;
                            planets = planetsCheckpoint;
                            new Toast(currentLocale.getString(UIField.ServerUnavailable.toString()), VisualisationPanel.this, Color.RED).start();
                        }
                    }
                    try {
                        Thread.sleep(frameTime);
                    } catch (InterruptedException ignored) {}
                }
            }
        }).start();

        receiveTask = () -> network.receiveNextResultPacket();
    }
    private Color getRandomColor() {
        Random rand = new Random();
        float r = rand.nextFloat();
        float g = rand.nextFloat();
        float b = rand.nextFloat();
        return new Color(r, g, b);
    }

    @Override
    public void setLocale(ListResourceBundle locale) {
        if (locale == null)
            throw new NullPointerException("Locale cannot be null");
        currentLocale = locale;
        new Localizer(localizedComponents).onLocaleChanged(locale);
        planetControls.setCurrentLocale(locale);
        processor.setLocale(locale);
    }

    @Override
    public ListResourceBundle getCurrentLocale() {
        return currentLocale;
    }

    private JPanel makeTopLine() {
        JPanel topLine = new JPanel();
        topLine.setLayout(new GridBagLayout());
        topLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, TablePanel.TOP_LINE_HEIGHT));



        ArrowButton toTable = new ArrowButton(currentLocale.getString(UIField.TableTitle.toString()), 0.2f, 1.5f);
        toTable.setFontSize(FONT_SIZE);
        toTable.setTextPadding(new Insets(0, 3, 5, 3));
        toTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                simulating = false;
                ((AuthPanelManager) getParent()).showNext();
            }
        });



        JTextView title = new JTextView();
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFontSize(TablePanel.TITLE_FONT_SIZE);
        title.setText(currentLocale.getString(UIField.VisualizationTitle.toString()));



        UserLoginView login = new UserLoginView();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                login.updateLogin();
            }
        });
        login.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                login.updateLogin();
                JDialog account = new UserSettings((newLocale) -> setLocale(newLocale));
                account.setLocation(GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().width / 2 - account.getWidth() / 2, GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().height / 2 - account.getHeight() / 2);
                account.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        if (!ClientCommandProcessor.getAuthStatus().getSuccess()) {
                            ((AuthPanelManager) getParent()).showAuthPanel();
                        }
                    }
                });
                account.setVisible(true);
            }
        });
        localizedComponents.put(toTable, UIField.TableTitle);
        localizedComponents.put(title, UIField.VisualizationTitle);



        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.NORTHWEST;
        constraints.gridwidth = 3;
        topLine.add(toTable, constraints);

        constraints.anchor = GridBagConstraints.CENTER;
        topLine.add(title, constraints);

        constraints.gridwidth = REMAINDER;
        constraints.anchor = GridBagConstraints.NORTHEAST;
        topLine.add(login, constraints);

        return topLine;
    }

    private JPanel makeVisualisationLine(){
        JPanel midLine = new JPanel();
        midLine.setLayout(new GridBagLayout());



        //Overrides paint to use this like canvas
        simulatingPanel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                //Drawing Background
                g.setColor(getBackground());
                g.fillRect(0, 0, VISUALIZATION_WIDTH + VISUALIZATION_PADDING * 2, VISUALIZATION_HEIGHT + VISUALIZATION_PADDING * 2);
                //Drawing frame/border for objects
                g.setColor(Color.WHITE);
                g.drawRect(VISUALIZATION_PADDING, VISUALIZATION_PADDING, VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT);

                for (Planet planet : planets) {
                    planet.draw((Graphics2D) g);
                    if (simulating)
                        planet.tick();
                }
                if (simulating)
                    for (int i = 0; i < planets.size(); i++) {
                        for (int l = i + 1; l < planets.size(); l++) {
                            planets.get(i).interact(planets.get(l));
                        }
                    }
            }
        };
        //Detecting clicks on objects and send it to planetControls
        simulatingPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(planets.size() == 0)
                    return;
                Planet closest = planets.get(0);
                ((CollectionTableModel)objInfo.getModel()).setDataVector(new Vector<>());//Clears table
                for (Planet planet : planets) {
                    planet.clearBorderColor();
                    if (planet.getDistance(e.getPoint()) < closest.getDistance(e.getPoint()))
                        closest = planet;
                }
                if (closest.getDistance(e.getPoint()) < Math.max(MINIMAL_TOUCHABLE_RADIUS, closest.getRadius())) {
                    closest.setBorderColor(Color.RED);
                    planetControls.setPlanet(closest);
                    String user = "";
                    Iterator<String> users = usersObjects.keySet().iterator();
                    while (users.hasNext() && (usersObjects.get(user) == null || !usersObjects.get(user).contains(closest.getRoute().getId())))
                        user = users.next();
                    int key = 0;
                    for(int objKey : collection.keySet())
                        if(collection.get(objKey).getId() == closest.getRoute().getId()){
                            key = objKey;
                            break;
                        }
                    ((CollectionTableModel)objInfo.getModel()).addRoute(key, closest.getRoute(), user);
                } else
                    planetControls.setPlanet(null);
            }
        });
        simulatingPanel.setPreferredSize(new Dimension(VISUALIZATION_WIDTH + VISUALIZATION_PADDING * 2, VISUALIZATION_HEIGHT + VISUALIZATION_PADDING * 2));
        simulatingPanel.setMaximumSize(new Dimension(VISUALIZATION_WIDTH + VISUALIZATION_PADDING * 2, VISUALIZATION_HEIGHT + VISUALIZATION_PADDING * 2));
        simulatingPanel.setBackground(Color.GRAY);



        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setMinimumSize(new Dimension((int) (MIN_PLANETS_CONTROL_WIDTH*TablePanel.MIN_SIZE_COEFFICIENT), 0));



        startButton = new JButton(currentLocale.getString(UIField.Start.toString()));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(startButton.isEnabled()) {
                    startButton.setText(simulating ? currentLocale.getString(UIField.Start.toString()) : currentLocale.getString(UIField.Stop.toString()));
                    simulating = !simulating;
                    if(simulating) {
                        simulationStartTime = System.currentTimeMillis();
                        planetsCheckpoint = new CopyOnWriteArrayList<>();
                        for(Planet pl : planets)
                            planetsCheckpoint.add(pl.getState());
                    }
                }
            }
        });
        localizedComponents.put(startButton, UIField.Start);
        planetControls = new PlanetControls(VISUALIZATION_WIDTH, VISUALIZATION_HEIGHT);

        controlPanel.add(startButton);
        controlPanel.add(planetControls);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.NONE;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.weightx = 0;

        midLine.add(simulatingPanel, constraints);

        constraints.fill = GridBagConstraints.VERTICAL;
        midLine.add(controlPanel, constraints);

        constraints.weightx = 1;
        constraints.fill = GridBagConstraints.BOTH;
        midLine.add(makeButtonsPanel(), constraints);

        return midLine;
    }

    private JLayeredPane makeButtonsPanel() {
        JLayeredPane panel = new JLayeredPane();
        JRhombusButton insert = makeButton(UIField.Add, SMALL_BUTTON_SIZE, Color.GREEN);
        insert.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processor.sendCommand(new CommandArgumentsPopup(new Insert()).getValidatedCommand());
                ((AuthPanelManager) getParent()).addTask(receiveTask);
            }
        });
        JRhombusButton clear = makeButton(UIField.Clear, SMALL_BUTTON_SIZE, Color.RED);
        clear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processor.sendCommand(new Clear());
                ((AuthPanelManager) getParent()).addTask(receiveTask);
            }
        });
        JRhombusButton remove = makeButton(UIField.Delete, SMALL_BUTTON_SIZE, Color.RED);
        remove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (planetControls.getPlanet() == null)
                    return;
                int key = 0;
                for(int objKey : collection.keySet())
                    if(collection.get(objKey).getId() == planetControls.getPlanet().getRoute().getId()){
                        key = objKey;
                        break;
                    }
                planetControls.setPlanet(null);
                ((CollectionTableModel)objInfo.getModel()).setDataVector(new Vector<>());
                processor.sendCommand(new RemoveByKey(), new String[]{key+""}).ifPresent(res -> {
                    if (!res.getSuccess())
                        new Toast(res.getFormattedString(), VisualisationPanel.this, Color.RED).start();
                    else
                        ((AuthPanelManager) getParent()).addTask(receiveTask);
                });
            }
        });


        insert.setBounds(0,0);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                clear.setBounds(0, (panel.getHeight() - clear.getHeight())/2);
                remove.setBounds(0, panel.getHeight() - remove.getHeight());
            }
        });



        panel.add(insert, JLayeredPane.DEFAULT_LAYER);
        panel.add(clear, JLayeredPane.DEFAULT_LAYER);
        panel.add(remove, JLayeredPane.DEFAULT_LAYER);
        return panel;
    }

    private JRhombusButton makeButton(UIField field, int size, Color borderColor){
        JRhombusButton button = new JRhombusButton(currentLocale.getString(field.toString()), MID_BUTTON_SIZE);
        button.setBackground(Main.TRANSPARENT);
        button.setFontSize(FONT_SIZE);
        button.setBorderColor(borderColor);
        button.setBorderWidth(BUTTON_BORDER_WIDTH);
        localizedComponents.put(button, field);
        return button;
    }
}
