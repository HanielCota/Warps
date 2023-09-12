package com.ankares.ankareswarps.config;

import com.zaxxer.hikari.HikariConfig;

public class DatabaseConfig {
    public HikariConfig createHikariConfig(String jdbcUrl, String username, String password) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setLeakDetectionThreshold(60000);
        return config;
    }
}
