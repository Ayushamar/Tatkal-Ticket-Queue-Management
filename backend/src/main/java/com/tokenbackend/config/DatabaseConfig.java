package com.tokenbackend.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Profile("production")
public class DatabaseConfig {

    @Value("${DATABASE_URL:}")
    private String databaseUrl;

    @Value("${DB_USERNAME:}")
    private String dbUsername;

    @Value("${DB_PASSWORD:}")
    private String dbPassword;

    @Bean
    public DataSource dataSource() throws URISyntaxException {
        HikariConfig config = new HikariConfig();
        
        // If the user pasted the raw Render URL (starts with postgres:// or postgresql://)
        if (databaseUrl != null && (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            URI dbUri = new URI(databaseUrl);
            
            // Extract username and password from the URL if present
            if (dbUri.getUserInfo() != null) {
                String[] userInfo = dbUri.getUserInfo().split(":");
                config.setUsername(userInfo[0]);
                if (userInfo.length > 1) {
                    config.setPassword(userInfo[1]);
                }
            } else {
                config.setUsername(dbUsername);
                config.setPassword(dbPassword);
            }
            
            int port = dbUri.getPort() != -1 ? dbUri.getPort() : 5432;
            // Force sslmode=disable for Render internal network
            String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + port + dbUri.getPath() + "?sslmode=disable";

            config.setJdbcUrl(dbUrl);
        } else {
            // Fallback for standard JDBC URL
            config.setJdbcUrl(databaseUrl);
            config.setUsername(dbUsername);
            config.setPassword(dbPassword);
        }

        config.setDriverClassName("org.postgresql.Driver");
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(5);
        config.setConnectionTimeout(30000);
        
        return new HikariDataSource(config);
    }
}
