package com.ankares.ankareswarps.repository;

import org.bukkit.Location;

public interface LocationRepository {

    void saveLocation(String locationName, Location location);

    void updateLocation(String locationName, Location location);

    void deleteLocation(String locationName);

    Location findLocationByName(String name);
}

