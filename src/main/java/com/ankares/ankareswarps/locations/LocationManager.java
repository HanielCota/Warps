package com.ankares.ankareswarps.locations;

import com.ankares.ankareswarps.WarpPlugin;
import com.ankares.ankareswarps.repository.LocationRepository;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

public class LocationManager {
    private final LocationRepository locationRepository;
    private final FileConfiguration config;
    private final WarpPlugin plugin;

    public LocationManager(WarpPlugin plugin, LocationRepository locationRepository) {
        this.plugin = plugin;
        this.locationRepository = locationRepository;
        this.config = plugin.getWarpConfig();
    }

    public void createLocation(String locationName, Location location) {
        if (locationRepository.findLocationByName(locationName) != null) {
            return;
        }

        saveLocationToConfig(locationName, location);

        locationRepository.saveLocation(locationName, location);
    }

    public Location getLocationByName(String name) {
        Location location = locationRepository.findLocationByName(name);
        if (location != null) {
            return location;
        }

        String path = "locations." + name;
        if (!config.contains(path)) {
            return null;
        }

        ConfigurationSection locationSection = config.getConfigurationSection(path);
        if (locationSection == null) {
            return null;
        }

        String worldName = locationSection.getString("world");
        if (worldName == null) {
            return null;
        }

        double x = locationSection.getDouble("x");
        double y = locationSection.getDouble("y");
        double z = locationSection.getDouble("z");
        float pitch = (float) locationSection.getDouble("pitch", 0.0);
        float yaw = (float) locationSection.getDouble("yaw", 0.0); // Obtém o valor do ângulo de rotação (yaw)

        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            return null;
        }

        location = new Location(world, x, y, z, yaw, pitch); // Define o yaw ao criar a localização

        locationRepository.saveLocation(name, location);

        return location;
    }


    public List<String> getAvailableWarps() {
        List<String> availableWarps = new ArrayList<>();
        ConfigurationSection locationSection = config.getConfigurationSection("locations");
        if (locationSection != null) {
            availableWarps.addAll(locationSection.getKeys(false));
        }
        return availableWarps;
    }

    public void deleteLocation(String name) {
        locationRepository.deleteLocation(name);
        config.set("locations." + name, null);
    }

    private void saveLocationToConfig(String locationName, Location location) {
        ConfigurationSection locationSection = config.createSection("locations." + locationName);
        locationSection.set("world", location.getWorld().getName());
        locationSection.set("x", location.getX());
        locationSection.set("y", location.getY());
        locationSection.set("z", location.getZ());
        locationSection.set("pitch", location.getPitch());
        locationSection.set("yaw", location.getYaw());
        plugin.getWarpConfig().save();
    }
}