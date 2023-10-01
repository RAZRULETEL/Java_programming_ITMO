package Client.ui.graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.ListResourceBundle;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Client.ui.components.LanguageSelect;
import Client.ui.components.Localizer;
import Client.ui.localization.UIField;

public class PlanetControls extends JPanel {
    private final static int SLIDER_TICK_SPACING_COUNT = 5;
    private final HashMap<JComponent, UIField> localizedComponents = new HashMap<>();
    private Planet planet;
    private Localizer localizer;
    private final JSlider coordX, coordY, mass, radius;
    private final JButton apply;
    public PlanetControls(int paneWidth, int paneHeight) {
        ListResourceBundle initLocale = LanguageSelect.getCurrentLocale();
        setLayout(new GridBagLayout());
        ChangeListener sliderTooltipSetter = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                ((JSlider)e.getSource()).setToolTipText(((JSlider)e.getSource()).getValue()+"");
            }
        };



        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.VERTICAL;
        constraints.weightx = 1;
        constraints.gridwidth = GridBagConstraints.REMAINDER;
        constraints.weighty = 1;

        JLabel xLabel = new JLabel(initLocale.getString(UIField.X.toString()));
        add(xLabel, constraints);
        localizedComponents.put(xLabel, UIField.X);
        coordX = new JSlider(0, paneWidth);
        coordX.setEnabled(false);
        coordX.setPaintTicks(true);
        coordX.setPaintLabels(true);
        coordX.setMajorTickSpacing(paneWidth/SLIDER_TICK_SPACING_COUNT);
        add(coordX, constraints);
        coordX.addChangeListener(sliderTooltipSetter);

        JLabel yLabel = new JLabel(initLocale.getString(UIField.Y.toString()));
        add(yLabel, constraints);
        localizedComponents.put(yLabel, UIField.Y);
        coordY = new JSlider(0, paneHeight);
        coordY.setEnabled(false);
        coordY.setPaintTicks(true);
        coordY.setPaintLabels(true);
        coordY.setMajorTickSpacing(paneWidth/SLIDER_TICK_SPACING_COUNT);
        add(coordY, constraints);
        coordY.addChangeListener(sliderTooltipSetter);

        JLabel massLabel = new JLabel(initLocale.getString(UIField.Mass.toString()));
        add(massLabel, constraints);
        localizedComponents.put(massLabel, UIField.Mass);
        mass = new JSlider(0, Integer.MAX_VALUE);
        mass.setEnabled(false);
        mass.setPaintTicks(true);
        mass.setPaintLabels(true);
        add(mass, constraints);
        mass.addChangeListener(sliderTooltipSetter);

        JLabel radiusLabel = new JLabel(initLocale.getString(UIField.Radius.toString()));
        add(radiusLabel, constraints);
        localizedComponents.put(radiusLabel, UIField.Radius);
        radius = new JSlider(0, 0);
        radius.setEnabled(false);
        radius.setPaintTicks(true);
        radius.setPaintLabels(true);
        add(radius, constraints);
        radius.addChangeListener(sliderTooltipSetter);

        constraints.weighty = 0;
        apply = new JButton(initLocale.getString(UIField.Save.toString()));
        add(apply, constraints);
        localizedComponents.put(apply, UIField.Save);

        apply.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(planet == null || !apply.isEnabled())
                    return;
                planet.setCoordinates(coordX.getValue(), coordY.getValue());
                planet.setMass(mass.getValue());
                planet.setRadius(radius.getValue());
            }
        });

        localizer = new Localizer(localizedComponents);
    }

    public void setPlanet(Planet planet){
        this.planet = planet;
        if(planet != null) {
            coordX.setValue(planet.getXOnPane());
            coordX.setEnabled(true);

            coordY.setValue(planet.getYOnPane());
            coordY.setEnabled(true);

            mass.setValue(planet.getMass());
            mass.setEnabled(true);

            radius.setMaximum(planet.getMaxRadius());
            radius.setMajorTickSpacing(planet.getMaxRadius()/SLIDER_TICK_SPACING_COUNT);
            radius.setValue(planet.getRadius());
            radius.setEnabled(true);
        }else{
            coordX.setEnabled(false);
            coordY.setEnabled(false);
            mass.setEnabled(false);
            radius.setEnabled(false);
        }
    }

    public Planet getPlanet(){
        return planet;
    }

    public void setCurrentLocale(ListResourceBundle newLocale) {
        localizer.onLocaleChanged(newLocale);
    }

    public void setEnabled(boolean enabled){
        apply.setEnabled(enabled);
    }
}
