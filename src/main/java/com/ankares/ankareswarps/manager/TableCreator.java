package com.ankares.ankareswarps.manager;

import com.ankares.ankareswarps.WarpPlugin;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.bukkit.Bukkit.getConsoleSender;

public class TableCreator {
    private final WarpPlugin plugin;

    public TableCreator(WarpPlugin plugin) {
        this.plugin = plugin;
    }

    public void createTable(String tableName, String createTableQuery) {
        try (Connection connection = plugin.getDatabaseManager().getConnection();
                Statement statement = connection.createStatement()) {
            String query = "CREATE TABLE IF NOT EXISTS " + tableName + " " + createTableQuery;
            int result = statement.executeUpdate(query);

            if (result >= 0) {
                getConsoleSender().sendMessage("§aTabela " + tableName + " criada com sucesso.");
                return;
            }

            getConsoleSender().sendMessage("Não foi possível criar a tabela " + tableName + ".");
        } catch (SQLException e) {
            getConsoleSender().sendMessage("Erro ao criar a tabela " + tableName + ": " + e.getMessage());
        }
    }
}