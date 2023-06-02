package Client.ui.graph;

import Client.command_processing.AsyncClientCommandProcessor;
import Client.ui.interfaces.PlanetChangeListener;
import Shared.commands.SyncPlanets;
import Shared.commands.interfaces.Command;

public class PlanetSynchronizer implements PlanetChangeListener {
    private final AsyncClientCommandProcessor processor;

    public PlanetSynchronizer(AsyncClientCommandProcessor processor) {
        this.processor = processor;
    }

    private final Command sync = new SyncPlanets();

    @Override
    public void onChange(Planet planet) {
        String[] args = new String[5];
        args[0] = planet.getRoute().getId() + "";
        args[1] = planet.getX() + "";
        args[2] = planet.getY() + "";
        args[3] = planet.getMass() + "";
        args[4] = planet.getSourceRadius() + "";
        sync.validate(args);
        processor.sendCommand(sync);
    }
}
