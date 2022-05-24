package com.mawuote.api.manager.module;

public enum ModuleCategory {
    COMBAT("Combat"),
    PLAYER("Player"),
    MISC("Misc"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    CLIENT("Client"),
    HUD("HUD");

    private final String name;

    ModuleCategory(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
