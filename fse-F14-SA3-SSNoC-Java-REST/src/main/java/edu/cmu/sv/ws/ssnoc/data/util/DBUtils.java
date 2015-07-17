package edu.cmu.sv.ws.ssnoc.data.util;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import org.h2.tools.DeleteDbFiles;

/**
 * This is a utility class to provide common functions to access and handle
 * Database operations.
 * 
 */
public class DBUtils {
	private static boolean DB_TABLES_EXIST = false;
	private static List<String> CREATE_TABLE_LST;
    private static boolean performaceRunning = false;

	static {
		CREATE_TABLE_LST = new ArrayList<String>();
		CREATE_TABLE_LST.add(SQL.CREATE_USERS);
        CREATE_TABLE_LST.add(SQL.CREATE_STATUS_CRUMB);
        CREATE_TABLE_LST.add(SQL.CREATE_CHAT);
        CREATE_TABLE_LST.add(SQL.CREATE_MEMORY_CRUMB);
	}

    /**
	 * This method will initialize the database.
	 * 
	 * @throws SQLException
	 */
	public static void initializeDatabase() throws SQLException {
        createTablesInDB();
	}

    public static void setPerformaceRunning() throws SQLException{
        performaceRunning = true;
        DB_TABLES_EXIST = false;
        Log.trace("Performace Test enabled");
        initializeDatabase();
    }

    public static boolean isPerformaceRunning(){
        return performaceRunning;
    }

    public static void stopPerformanceRunning() throws SQLException{
        Log.trace("Shutting Down Performance Test Database.......");
        getConnection().createStatement().execute("SHUTDOWN");
        Log.trace("Deleting Performance Test Database.......");
        DeleteDbFiles.execute("~", "h2dbPerf", true);
        Log.trace("Performance Test Database Deleted ");
        performaceRunning = false;
    }

	/**
	 * This method will create necessary tables in the database.
	 * 
	 * @throws SQLException
	 */
	protected static void createTablesInDB() throws SQLException {
		Log.enter();
		if (DB_TABLES_EXIST) {
			return;
		}


        final List<String> CORE_TABLE_LST= Arrays.asList(SQL.SSN_USERS,SQL.SSN_STATUS_CRUMB,SQL.SSN_CHAT,SQL.SSN_MEMORY_CRUMB);

        for (String CORE_TABLE_NAME : CORE_TABLE_LST){
        try (Connection conn = getConnection();
			Statement stmt = conn.createStatement();){
			if (!doesTableExistInDB(conn, CORE_TABLE_NAME)) {
				Log.info("Creating tables in database ...");
				for (String query : CREATE_TABLE_LST) {
					Log.debug("Executing query: " + query);
					boolean status = stmt.execute(query);
					Log.debug("Query execution completed with status: "
							+ status);
				}

				Log.info("Tables created successfully");
			} else {
				Log.info("Tables already exist in database. Not performing any action.");
			}

			DB_TABLES_EXIST = true;
		}}
		Log.exit();
	}

	/**
	 * This method will check if the table exists in the database.
	 * 
	 * @param conn
	 *            - Connection to the database
	 * @param tableName
	 *            - Table name to check.
	 * 
	 * @return - Flag whether the table exists or not.
	 * 
	 * @throws SQLException
	 */
	public static boolean doesTableExistInDB(Connection conn, String tableName)
			throws SQLException {

            Log.enter(tableName);



		if (conn == null || tableName == null || "".equals(tableName.trim())) {
			Log.error("Invalid input parameters. Returning doesTableExistInDB() method with FALSE.");
			return false;
		}

		boolean tableExists = false;

		final String SELECT_QUERY = SQL.CHECK_TABLE_EXISTS_IN_DB;

		ResultSet rs = null;
		try (PreparedStatement selectStmt = conn.prepareStatement(SELECT_QUERY)) {
			selectStmt.setString(1, tableName.toUpperCase());
			rs = selectStmt.executeQuery();
			int tableCount = 0;
			if (rs.next()) {
				tableCount = rs.getInt(1);
			}

			if (tableCount > 0) {
				tableExists = true;
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}

		Log.exit(tableExists);

		return tableExists;
	}

	/**
	 * This method returns a database connection from the Hikari CP Connection
	 * Pool
	 * 
	 * @return - Connection to the H2 database
	 * 
	 * @throws SQLException
	 */
	public static final Connection getConnection() throws SQLException {
        if(performaceRunning){
            IConnectionPool cp = ConnectionPoolFactory.getInstance().getPerfTestConnectionPool();
            return cp.getConnection();
        }
		IConnectionPool cp = ConnectionPoolFactory.getInstance().getH2ConnectionPool();
		return cp.getConnection();
	}
}
