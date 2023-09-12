package com.ankares.ankareswarps.repository;

import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLocationRepository implements LocationRepository {
    private final Map<String, Location> locations = new HashMap<>();

    @Override
    public void saveLocation(String locationName, Location location) {
        locations.put(locationName, location);
    }

    @Override
    public void updateLocation(String locationName, Location location) {
        locations.put(locationName, location);
    }

    @Override
    public void deleteLocation(String locationName) {
        locations.remove(locationName);
    }

    @Override
    public Location findLocationByName(String name) {
        return locations.get(name);
    }
}
