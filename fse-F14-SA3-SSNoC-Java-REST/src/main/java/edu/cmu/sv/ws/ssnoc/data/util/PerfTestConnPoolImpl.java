package edu.cmu.sv.ws.ssnoc.data.util;

/**
 * Created by Vignan on 10/19/2014.
 */

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.PropertyUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This class is the Connection Pool implementation for the H2 database.
 *
 */
public class PerfTestConnPoolImpl implements IConnectionPool {
    private static PerfTestConnPoolImpl instance = null;
    private HikariDataSource ds = null;

    static {
        try {
            Log.info("Initializing the connection pool ... ");
            instance = new PerfTestConnPoolImpl();
            Log.info("Connection pool initialized successfully.");
        } catch (Exception e) {
            Log.error(
                    "Exception when trying to initialize the connection pool",
                    e);
        }
    }

    /**
     * Constructor to initialize H2 connection pool.
     */
    private PerfTestConnPoolImpl() {
        HikariConfig config = new HikariConfig();

        config.setDataSourceClassName("org.h2.jdbcx.JdbcDataSource");
        config.addDataSourceProperty("URL", PropertyUtils.PERF_DB_CONN_URL);
        config.addDataSourceProperty("user", PropertyUtils.DB_USERNAME);
        config.addDataSourceProperty("password", PropertyUtils.DB_PASSWORD);

        ds = new HikariDataSource(config);
        ds.setMaximumPoolSize(PropertyUtils.DB_CONNECTION_POOL_SIZE);
    }

    public static PerfTestConnPoolImpl getInstance() {
        return instance;
    }

    /**
     * This method will return the connection object to the database.
     */
    public Connection getConnection() throws SQLException {
        if (ds == null) {
            Log.error("HikariDataSource is NULL. Nooooooooooo :)");
            return null;
        }

        return ds.getConnection();
    }
}
