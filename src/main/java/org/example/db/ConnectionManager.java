package org.example.db;
import com.zaxxer.hikari.HikariDataSource;


public interface ConnectionManager {
    HikariDataSource getDataSource();
}
