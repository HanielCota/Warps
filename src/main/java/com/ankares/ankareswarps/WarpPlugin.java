package com.ankares.ankareswarps;

import co.aikar.commands.PaperCommandManager;
import com.ankares.ankareswarps.cache.PlayerDataCacheProvider;
import com.ankares.ankareswarps.commands.*;
import com.ankares.ankareswarps.external.fastinv.FastInvManager;
import com.ankares.ankareswarps.locations.LocationManager;
import com.ankares.ankareswarps.manager.CooldownManager;
import com.ankares.ankareswarps.manager.DatabaseManager;
import com.ankares.ankareswarps.manager.PlayerDataManager;
import com.ankares.ankareswarps.manager.TableCreator;
import com.ankares.ankareswarps.repository.InMemoryLocationRepository;
import com.ankares.ankareswarps.repository.PlayerDataCache;
import com.ankares.ankareswarps.utils.ConfigUtils;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public final class WarpPlugin extends JavaPlugin {

    private LocationManager locationManager;
    private ConfigUtils warpConfig;
    private ConfigUtils databaseConfig;
    private DatabaseManager databaseManager;
    private TableCreator tableCreator;
    private PlayerDataManager playerDataManager;
    private PlayerDataCache playerDataCache;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        setupConfig();
        initializeDatabase();
        createTables();
        injectClasses();
        initializeLocationManager();
        initializeCommands();
    }

    private void setupConfig() {
        warpConfig = new ConfigUtils(this, "warps.yml");
        databaseConfig = new ConfigUtils(this, "database.yml");
    }

    private void injectClasses() {
        playerDataCache = new PlayerDataCacheProvider();
        playerDataManager = new PlayerDataManager(databaseManager.getDataSource(), playerDataCache);
        cooldownManager = new CooldownManager(3);
        FastInvManager.register(this);
    }

    private void initializeLocationManager() {
        InMemoryLocationRepository locationRepository = new InMemoryLocationRepository();
        locationManager = new LocationManager(this, locationRepository);
    }

    private void initializeDatabase() {
        ConfigurationSection databaseConfiguration = databaseConfig.getConfigurationSection("database");

        if (databaseConfiguration == null) {
            getLogger().warning("Não foi possível localizar a configuração do banco de dados (database.yml).");
            return;
        }

        String jdbcUrl = databaseConfiguration.getString("jdbcUrl");
        String username = databaseConfiguration.getString("username");
        String password = databaseConfiguration.getString("password");

        if (jdbcUrl == null || username == null || password == null) {
            getLogger().warning("Algumas configurações de banco de dados estão ausentes ou inválidas.");
            return;
        }

        databaseManager = new DatabaseManager(jdbcUrl, username, password);
        getLogger().info("O banco de dados foi ativado com sucesso.");
    }

    private void createTables() {
        tableCreator = new TableCreator(this);
        tableCreator.createTable(
                "player_data",
                "(id INT AUTO_INCREMENT PRIMARY KEY, player_name VARCHAR(255), warp_name TEXT, save_date TIMESTAMP NOT NULL, data TEXT)");
    }

    private void initializeCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        registerCommands(commandManager);
    }

    private void registerCommands(PaperCommandManager commandManager) {
        commandManager.registerCommand(new WarpCommand());
        commandManager.registerCommand(new SetWarpCommand());
        commandManager.registerCommand(new DeleteWarpCommand());
        commandManager.registerCommand(new WarpListCommand());
        commandManager.registerCommand(new WarpPreferCommand(playerDataManager));
    }
}
