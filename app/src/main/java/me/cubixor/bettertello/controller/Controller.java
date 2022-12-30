package me.cubixor.bettertello.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import me.cubixor.bettertello.tello.TelloAction;

public class Controller implements Serializable {

    private final int inAppID;
    private final String descriptor;
    private final String name;
    private final Map<Integer, TelloAction> mappings = new LinkedHashMap<>();

    public Controller(String descriptor, String name, int inAppID) {
        this.descriptor = descriptor;
        this.name = name;
        this.inAppID = inAppID;
    }

    public int getInAppID() {
        return inAppID;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public String getName() {
        return name;
    }

    public Map<Integer, TelloAction> getMappings() {
        return mappings;
    }

    public int getKeyByAction(TelloAction telloAction) {
        for (int key : mappings.keySet()) {
            if (mappings.get(key).equals(telloAction)) {
                return key;
            }
        }
        return -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Controller that = (Controller) o;
        return Objects.equals(descriptor, that.descriptor);
    }

    @Override
    public int hashCode() {
        return Objects.hash(descriptor);
    }
}
