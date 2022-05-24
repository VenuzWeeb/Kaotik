package com.mawuote.api.manager.event;

public class Event extends net.minecraftforge.fml.common.eventhandler.Event {
    private boolean cancelled;
    private Stage stage;

    public Event() {

    }

    public Event(Stage stage) {
        this.stage = stage;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    public Stage getStage() {
        return stage;
    }

    public boolean isPre(){
        return stage == Stage.PRE;
    }

    public boolean isPost(){
        return stage == Stage.POST;
    }

    public enum Stage {
        PRE, POST
    }
}