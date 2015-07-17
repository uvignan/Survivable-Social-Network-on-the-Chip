package edu.cmu.sv.ws.ssnoc.data.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;


public class SearchDAOImpl extends BaseDAOImpl implements ISearchDAO {
	
    public String QueryName(String[] query){
        Log.enter();

        String mergedArray = "";
        for(int i=0; i < query.length;i++){
        	mergedArray += query[i];
        	mergedArray += "";
        }
        
        String names = null ;
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                .prepareStatement(SQL.SEARCH_USER_BY_NAME)) {
            stmt.setString(1, mergedArray);

            names = processQuery(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(names);
        }
        return names;
    }
    
    private String processQuery(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside process with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        
        char quotes ='"';
        String res = "[";    

        try (ResultSet rs = stmt.executeQuery()) {
        	ResultSetMetaData rsmd = rs.getMetaData();
        	int columnCount = rsmd.getColumnCount();
            while (rs.next()) {
            	res += "{";
            	for(int i=1; i < columnCount+1; i++){
            		String columnName = rsmd.getColumnName(i);
            		res += quotes + columnName + quotes + ":" + quotes;
            		String columnVal = rs.getString(i);
            		res += columnVal + quotes;
            		if(i != columnCount) res += ",";
            	}
            	res += "},";
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(res);
        }
        res = res.substring(0, res.length()-1);
        if(!res.equals("")){res = res + "]";}
        return res;
    }
    
    public String QueryStatus(String[] query){
        Log.enter();

        String mergedArray = "";
        for(int i=0; i < query.length;i++){
        	mergedArray += query[i];
        	mergedArray += "";
        }
        
        String names = null ;
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                .prepareStatement(SQL.SEARCH_USER_BY_STATUS)) {
            stmt.setString(1, mergedArray);

            names = processQuery(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(names);
        }
        return names;
    }
    
    public String QueryAnnouncements(String[] query){
        Log.enter();

        String mergedArray = "%";
        for(int i=0; i < query.length;i++){
        	mergedArray += query[i];
        	mergedArray += "%";
        }
        
        String ancmnts = null ;
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                .prepareStatement(SQL.SEARCH_ANNOUNCEMENT)) {
            stmt.setString(1, mergedArray);

            ancmnts = processQuery(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(ancmnts);
        }
        return ancmnts;
    }
    
    public String QueryPublicMessage(String[] query){
        Log.enter();

        String mergedArray = "%";
        for(int i=0; i < query.length;i++){
        	mergedArray += query[i];
        	mergedArray += "%";
        }
        
        String pub_message = null ;
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                .prepareStatement(SQL.SEARCH_PUBLIC_MESSAGE)) {
            stmt.setString(1, mergedArray);

            pub_message = processQuery(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(pub_message);
        }
        return pub_message;
    }
    
    public String QueryPrivateMessages(String[] query){
        Log.enter();

        String mergedArray = "%";
        for(int i=0; i < query.length;i++){
        	mergedArray += query[i];
        	mergedArray += "%";
        }
        
        String pri_message = null ;
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                .prepareStatement(SQL.SEARCH_PRIVATE_MESSAGE)) {
            stmt.setString(1, mergedArray);

            pri_message = processQuery(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(pri_message);
        }
        return pri_message;
    }
}
