package com.ankares.ankareswarps.manager;

import com.ankares.ankareswarps.repository.PlayerDataCache;
import com.ankares.ankareswarps.repository.PlayerDataRepository;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.bukkit.Bukkit.getLogger;

public class PlayerDataManager implements PlayerDataRepository {
    private final Logger logger = getLogger();
    private final HikariDataSource dataSource;
    private final PlayerDataCache playerDataCache;

    public PlayerDataManager(HikariDataSource dataSource, PlayerDataCache playerDataCache) {
        this.dataSource = dataSource;
        this.playerDataCache = playerDataCache;
    }

    public boolean hasPlayerData(String playerName) {
        String cachedData = playerDataCache.getIfPresent(playerName);
        if (cachedData != null) {
            return true;
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT COUNT(*) FROM player_data WHERE player_name = ?")) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao acessar o banco de dados", e);
        }

        return false;
    }

    public void savePlayerData(String playerName, String data) {
        try (Connection connection = dataSource.getConnection()) {
            if (hasPlayerData(playerName)) {
                updatePlayerData(playerName, data, connection);
                return;
            }

            insertPlayerData(playerName, data, connection);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao acessar o banco de dados", e);
        }
    }

    private void insertPlayerData(String playerName, String data, Connection connection) {
        String query = "INSERT INTO player_data (player_name, data) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.setString(2, data);
            statement.executeUpdate();
            playerDataCache.put(playerName, data);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao inserir os dados do jogador", e);
        }
    }

    public void savePlayerFavoriteWarpWithDate(String playerName, String warpName, String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Convert the string into a Date object
            Date date = dateFormat.parse(dateString);

            try (Connection connection = dataSource.getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "INSERT INTO player_data (player_name, warp_name, save_date) VALUES (?, ?, ?)")) {

                statement.setString(1, playerName);
                statement.setString(2, warpName);

                // Format the Date object to "yyyy-MM-dd"
                statement.setDate(3, new java.sql.Date(date.getTime()));

                statement.executeUpdate();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Ocorreu um erro ao adicionar a warp favorita do jogador", e);
            }
        } catch (ParseException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao fazer o parsing da data a partir da string", e);
        }
    }

    public Date getPlayerFavoriteWarpSaveDate(String playerName, String warpName) {
        String query = "SELECT DATE(save_date) FROM player_data WHERE player_name = ? AND warp_name = ?";

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, playerName);
            statement.setString(2, warpName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDate(1); // Retrieve the date without time
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "An error occurred while getting the save date of the player's favorite warp", e);
        }
        return null;
    }

    private void updatePlayerData(String playerName, String data, Connection connection) {
        String query = "UPDATE player_data SET data = ? WHERE player_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data);
            statement.setString(2, playerName);
            statement.executeUpdate();
            playerDataCache.put(playerName, data);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao atualizar os dados do jogador", e);
        }
    }

    public void removePlayerFavoriteWarp(String playerName, String warpName) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "DELETE FROM player_data WHERE player_name = ? AND warp_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao remover a warp favorita do jogador", e);
        }
    }

    public String getPlayerData(String playerName) {
        String cachedData = playerDataCache.getIfPresent(playerName);
        if (cachedData != null) {
            return cachedData;
        }

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT data FROM player_data WHERE player_name = ?")) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String data = resultSet.getString("data");
                    playerDataCache.put(playerName, data);
                    return data;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao acessar o banco de dados", e);
        }

        return null;
    }

    public void removeAllPlayerFavoriteWarps(String playerName) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "DELETE FROM player_data WHERE player_name = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao remover todas as warps favoritas do jogador", e);
        }
    }

    public void addPlayerFavoriteWarp(String playerName, String warpName) {
        try (Connection connection = dataSource.getConnection()) {
            String query = "INSERT INTO player_data (player_name, warp_name) VALUES (?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, playerName);
                statement.setString(2, warpName);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao adicionar a warp favorita do jogador", e);
        }
    }

    public List<String> getPlayerFavoriteWarps(String playerName) {
        // No need to cache this result as it may change frequently
        List<String> favoriteWarps = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT warp_name FROM player_data WHERE player_name = ?")) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String warpName = resultSet.getString("warp_name");
                    favoriteWarps.add(warpName);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao acessar o banco de dados", e);
        }

        return favoriteWarps;
    }

    public int getPlayerFavoriteWarpsCount(String playerName) {
        try (Connection connection = dataSource.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement("SELECT COUNT(*) FROM player_data WHERE player_name = ?")) {
            statement.setString(1, playerName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ocorreu um erro ao acessar o banco de dados", e);
        }

        return 0;
    }
}
