package org.example.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;


public class ConnectionManagerImpl implements ConnectionManager {

    private final HikariDataSource dataSource;


    public ConnectionManagerImpl(HikariConfig config) {
        this.dataSource = new HikariDataSource(config);
    }

    public ConnectionManagerImpl(HikariDataSource dataSource) { // Конструктор для заглушки
        this.dataSource = dataSource;
    }

    public HikariDataSource getDataSource() {
        return dataSource;
    }
}
