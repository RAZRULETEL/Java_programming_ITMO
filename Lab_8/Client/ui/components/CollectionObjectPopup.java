package Client.ui.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ListResourceBundle;
import java.util.Optional;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Client.ui.Toast;
import Client.ui.components.texts.JTextFieldBeautiful;
import Client.ui.localization.UIField;
import Shared.resources.Coordinates;
import Shared.resources.Location;
import Shared.resources.Route;
import Shared.resources.RouteBuilder;

public class CollectionObjectPopup extends JDialog {
    private static final Color ERROR_COLOR = Color.RED, CORRECT_COLOR = Color.GREEN;
    private static final int BORDER_RADIUS = 25, FONT_SIZE = 16, MIN_WIDTH = 400, MIN_HEIGHT = 800;
    private Route route;

    public CollectionObjectPopup(Route route, ListResourceBundle locale) {
        super((java.awt.Frame) null, true);
        boolean makeNew = route == null;
        if (makeNew) {
            route = new RouteBuilder().build();
            route.setTo(null);
        }
        this.route = null;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        add(panel);
        setMinimumSize(new Dimension(MIN_WIDTH, MIN_HEIGHT));

        KeyAdapter doubleValidator = makeValidator((val) -> checkDouble(val).isPresent());
        KeyAdapter floatValidator = makeValidator((val) -> checkFloat(val).isPresent());
        KeyAdapter intValidator = makeValidator((val) -> checkInt(val).isPresent());

        panel.add(new JLabel(locale.getString(UIField.Name.toString())));
        JTextField name = makeField(route.getName() + "", makeValidator((str) -> !str.isEmpty()));
        panel.add(name);

        panel.add(new JLabel(locale.getString(UIField.CoordsX.toString())));
        JTextField coords_x = makeField(route.getCoordinates().getX() + "", makeValidator((str) -> checkDouble(str).isPresent() && checkDouble(str).get() > Coordinates.MIN_X));
        panel.add(coords_x);

        panel.add(new JLabel(locale.getString(UIField.CoordsY.toString())));
        JTextField coords_y = makeField(route.getCoordinates().getY() + "", intValidator);
        panel.add(coords_y);

        panel.add(new JLabel(locale.getString(UIField.StartX.toString())));
        JTextField from_x = makeField(route.getFrom().getX() + "", doubleValidator);
        panel.add(from_x);

        panel.add(new JLabel(locale.getString(UIField.StartY.toString())));
        JTextField from_y = makeField(route.getFrom().getY() + "", floatValidator);
        panel.add(from_y);

        panel.add(new JLabel(locale.getString(UIField.StartZ.toString())));
        JTextField from_z = makeField(route.getFrom().getZ() + "", doubleValidator);
        panel.add(from_z);

        panel.add(new JLabel(locale.getString(UIField.StartName.toString())));
        JTextField from_name = makeField(route.getFrom().getName() + "", makeValidator((str) -> true));
        panel.add(from_name);


        panel.add(new JLabel(locale.getString(UIField.FinishX.toString())));
        JTextField to_x = makeField(route.getTo() == null ? "" : route.getTo().getX() + "", makeValidator((val) -> val.isEmpty() || checkDouble(val).isPresent()));
        panel.add(to_x);

        panel.add(new JLabel(locale.getString(UIField.FinishY.toString())));
        JTextField to_y = makeField(route.getTo() == null ? "" : route.getTo().getY() + "", makeValidator((val) -> (to_x.getText().isEmpty() || val.isEmpty()) || checkFloat(val).isPresent()));
        panel.add(to_y);

        panel.add(new JLabel(locale.getString(UIField.FinishZ.toString())));
        JTextField to_z = makeField(route.getTo() == null ? "" : route.getTo().getZ() + "", makeValidator((val) -> (to_x.getText().isEmpty() || val.isEmpty()) || checkDouble(val).isPresent()));
        panel.add(to_z);

        panel.add(new JLabel(locale.getString(UIField.FinishName.toString())));
        JTextField to_name = makeField(route.getTo() == null ? "" : route.getTo().getName() + "", makeValidator((str) -> true));
        panel.add(to_name);


        to_x.addKeyListener(new KeyAdapter() {
            boolean wasEmpty = false;

            @Override
            public void keyReleased(KeyEvent e) {
                if (to_x.getText().isEmpty()) {
                    ((JTextFieldBeautiful) to_y).setBorderColor(CORRECT_COLOR);
                    ((JTextFieldBeautiful) to_z).setBorderColor(CORRECT_COLOR);
                    wasEmpty = true;
                } else if (wasEmpty) {
                    wasEmpty = false;
                    for (KeyListener listener : to_y.getKeyListeners())
                        listener.keyReleased(new KeyEvent(to_y, 0, System.currentTimeMillis(), 0, 0));
                    for (KeyListener listener : to_z.getKeyListeners())
                        listener.keyReleased(new KeyEvent(to_z, 0, System.currentTimeMillis(), 0, 0));

                }
            }
        });

        panel.add(new JLabel(locale.getString(UIField.Distance.toString())));
        JTextField dist = makeField(route.getDistance() + "", makeValidator((val) -> val.isEmpty() || (checkInt(val).isPresent() && checkInt(val).get() > Route.MIN_DISTANCE)));
        panel.add(dist);


        JButton button = new JButton(locale.getString(UIField.Save.toString()));
        Route finalRoute = route;
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    finalRoute.setName(name.getText());
                    finalRoute.getCoordinates().setX(Double.parseDouble(coords_x.getText().replaceAll(",", ".")));
                    finalRoute.getCoordinates().setY(Integer.parseInt(coords_y.getText()));
                    Location from = finalRoute.getFrom();
                    from.setX(Double.parseDouble(from_x.getText().replaceAll(",", ".")));
                    from.setY(Float.parseFloat(from_y.getText().replaceAll(",", ".")));
                    from.setZ(Double.parseDouble(from_z.getText().replaceAll(",", ".")));
                    from.setName(from_name.getText());
                    if (!to_x.getText().equals("")) {
                        Location to = finalRoute.getTo() == null ? new Location() : finalRoute.getTo();
                        to.setX(Double.parseDouble(to_x.getText().replaceAll(",", ".")));
                        to.setY(Float.parseFloat(to_y.getText().replaceAll(",", ".")));
                        to.setZ(Double.parseDouble(to_z.getText().replaceAll(",", ".")));
                        to.setName(to_name.getText());
                        finalRoute.setTo(to);
                    } else
                        finalRoute.setTo(null);
                    finalRoute.setDistance(dist.getText().equals("") ? null : Integer.valueOf(dist.getText()));
                    CollectionObjectPopup.this.route = finalRoute;
                    CollectionObjectPopup.this.dispose();
                } catch (NumberFormatException ignored) {
                } catch (IllegalArgumentException ex) {
                    new Toast(ex.getMessage(), panel, ERROR_COLOR).start();
                }
            }
        });
        panel.add(button);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                CollectionObjectPopup.this.route = null;
                dispose();
            }
        });
        setVisible(true);
    }

    public JTextFieldBeautiful makeField(String text, KeyListener inputHandler) {
        JTextFieldBeautiful field = makeField(text);
        field.addKeyListener(inputHandler);
        return field;
    }

    private JTextFieldBeautiful makeField(String text) {
        JTextFieldBeautiful field = new JTextFieldBeautiful("");
        field.setText(text);
        field.setFontSize(FONT_SIZE);
        field.setBorderRadius(BORDER_RADIUS);
        return field;
    }

    public Route getRoute() {
        return route;
    }

    private KeyAdapter makeValidator(Predicate<String> validator) {
        return new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextFieldBeautiful thisButton = (JTextFieldBeautiful) e.getSource();
                if (validator.test(thisButton.getText()))
                    thisButton.setBorderColor(CORRECT_COLOR);
                else
                    thisButton.setBorderColor(ERROR_COLOR);
            }
        };
    }

    private static Optional<Double> checkDouble(String str) {
        try {
            return Optional.of(Double.parseDouble(str.replaceAll(",", ".").trim()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private static Optional<Float> checkFloat(String str) {
        try {
            return Optional.of(Float.parseFloat(str.replaceAll(",", ".").trim()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    private static Optional<Integer> checkInt(String str) {
        try {
            return Optional.of(Integer.parseInt(str.trim()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}