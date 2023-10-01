package Shared.command_processing;

public interface SynchronizationProvider {
    void synchronizeRoute(int id, double x, double y, int mass, int radius);
}
