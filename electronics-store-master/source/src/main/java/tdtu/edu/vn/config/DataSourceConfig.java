package tdtu.edu.vn.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@SuppressWarnings("LombokGetterMayBeUsed")
@Component
public class DataSourceConfig {
    private static DataSourceConfig instance;
    private final DataSource dataSource;

    private DataSourceConfig() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        config.setJdbcUrl("jdbc:mysql://localhost:3306/electronics-store");
        config.setUsername("root");
        config.setPassword("");
        dataSource = new HikariDataSource(config);
    }

    public static synchronized DataSourceConfig getInstance() {
        if (instance == null) {
            instance = new DataSourceConfig();
        }
        return instance;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}