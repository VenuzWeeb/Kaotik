package com.mawuote.api.manager.value;

public class Value {
    private final String name;
    private final String tag;
    private final String description;

    public Value(String name, String tag, String description) {
        this.name = name;
        this.tag = tag;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public String getDescription() {
        return description;
    }
}