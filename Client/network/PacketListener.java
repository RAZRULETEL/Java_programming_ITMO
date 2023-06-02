package Client.network;

import Shared.command_processing.ResultDTO;

public interface PacketListener {
    void onPacketReceived(ResultDTO packet);
}
