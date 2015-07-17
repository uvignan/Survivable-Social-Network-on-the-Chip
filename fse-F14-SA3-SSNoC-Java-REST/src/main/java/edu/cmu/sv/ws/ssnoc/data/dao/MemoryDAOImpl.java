package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;

import java.io.File;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vignan on 10/14/2014.
 */
public class MemoryDAOImpl extends BaseDAOImpl implements IMemoryDAO {

    @Override
    public void insertMemoryStats(MemoryPO mpo){

        if (mpo == null) {
            Log.warn("Inside save method with userPO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_MEMORY_STATS)) {
            stmt.setLong(1, mpo.getUsedVolatile());
            stmt.setLong(2, mpo.getRemainingVolatile());
            stmt.setLong(3, mpo.getUsedPersistent());
            stmt.setLong(4, mpo.getRemainingPersistent());
            stmt.setString(5,mpo.getCreatedAt());

            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

   @Override
   public void deleteMemoryCrumbData(){
       boolean status = false;

       String truncateTable = SQL.DELETE_MEMORY_STATS;
       try (Connection conn = getConnection();
            Statement stmt = conn.createStatement();) {
           Log.debug("Executing query: " + stmt);
           status = stmt.execute(truncateTable);
           Log.debug("Query execution completed with status: "
                   + status);
           Log.info("Data truncated successfully");
       } catch (SQLException e) {
           handleException(e);
           Log.exit(status);
       }
   }

    @Override
    public List<MemoryPO> getMemoryStats(String toDate, String fromDate){
        String query = SQL.GET_MEMORY_STATS;

        List<MemoryPO> memstatsPO = new ArrayList<MemoryPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, fromDate);
            stmt.setString(2, toDate);
            memstatsPO = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(memstatsPO);
        }

        return memstatsPO;
    }

    private List<MemoryPO> processResults(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processResults method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<MemoryPO> memStats = new ArrayList<MemoryPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                MemoryPO po = new MemoryPO();
                po.setUsedVolatile(rs.getLong(1));
                po.setRemainingVolatile(rs.getLong(2));
                po.setUsedPersistent(rs.getLong(3));
                po.setRemainingPersistent(rs.getLong(4));
                po.setCreatedAt(rs.getString(5));

                memStats.add(po);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(memStats);
        }

        return memStats;
    }
}
