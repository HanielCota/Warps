package com.ankares.ankareswarps.services;

import com.ankares.ankareswarps.repository.LocationRepository;
import org.bukkit.Location;

public class LocationService {
    private final LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public void saveLocation(String locationName, Location location) {
        locationRepository.saveLocation(locationName, location);
    }

    public void updateLocation(String locationName, Location location) {
        locationRepository.updateLocation(locationName, location);
    }

    public void deleteLocation(String locationName) {
        locationRepository.deleteLocation(locationName);
    }

    public Location findLocationByName(String name) {
        return locationRepository.findLocationByName(name);
    }
}
