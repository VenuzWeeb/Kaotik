package com.mawuote.api.manager.event.impl.network;

import com.mawuote.api.manager.event.Event;
import net.minecraft.network.Packet;

/**
 * @author SrRina
 * @since 10/10/2020 at 17:04
 */
public class EventPacket extends Event {
    private Packet packet;

    public static class Send extends EventPacket {
        public Send(Event.Stage stage, Packet packet) {
            super(stage, packet);
        }
    }

    public static class Receive extends EventPacket {
        public Receive(Event.Stage stage, Packet packet) {
            super(stage, packet);
        }
    }

    public EventPacket(Stage stage, Packet packet) {
        super(stage);

        this.packet = packet;
    }

    private void setPacket(Packet packet) {
        this.packet = packet;
    }

    public Packet getPacket() {
        return packet;
    }
}
