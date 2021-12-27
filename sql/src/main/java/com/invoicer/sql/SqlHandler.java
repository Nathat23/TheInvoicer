package com.invoicer.sql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class SqlHandler {

    private final String url;
    private final String username;
    private final String password;
    private HikariDataSource hikariDataSource;

    public SqlHandler(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public void initialise() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        // apparently use DataSource classes instead?
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDataSource = new HikariDataSource(hikariConfig);
    }


}
