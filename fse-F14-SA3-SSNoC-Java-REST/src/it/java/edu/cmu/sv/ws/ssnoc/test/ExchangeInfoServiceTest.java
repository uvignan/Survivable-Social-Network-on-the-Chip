package edu.cmu.sv.ws.ssnoc.test;

import edu.cmu.sv.ws.ssnoc.rest.ExchangeInfoService;
import edu.cmu.sv.ws.ssnoc.rest.ExchangeMessageService;
import org.junit.Test;

import static org.junit.Assert.*;


import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;
import javax.ws.rs.core.Response;

import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;


public class ExchangeInfoServiceTest {
    @BeforeClass
    public static void setUpTestData() throws Exception{

        DBUtils.setPerformaceRunning();
        Connection conn= DBUtils.getConnection();

        PreparedStatement stmtInsertUser = conn.prepareStatement(SQL.INSERT_USER);
        stmtInsertUser.setString(1, "A");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, "1");
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        stmtInsertUser.setString(1, "B");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, "1");
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        stmtInsertUser.setString(1, "C");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        
        
        ExchangeMessageService EMS = new ExchangeMessageService();
        String username = "A";
        ExchangeInfo message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("wall message from A");
        Response res = EMS.addWallMessage(username, message);
        
        EMS = new ExchangeMessageService();
        username = "C";
        message = new ExchangeInfo();
        message.setAuthor("C");
        message.setContent("wall message from C");
        res = EMS.addWallMessage(username, message);
        
        EMS = new ExchangeMessageService();
        username = "A";
        message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("announcement message from A");
        res = EMS.addAnnouncement(username, message);
        
        EMS = new ExchangeMessageService();
        username = "C";
        message = new ExchangeInfo();
        message.setAuthor("C");
        message.setContent("announcement message from C");
        res = EMS.addAnnouncement(username, message);
       
        
        EMS = new ExchangeMessageService();
        String username1 = "A";
        String username2 = "B";
        message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("chat from A to B");
        res = EMS.addChatMessage(username1,username2, message);
        
        EMS = new ExchangeMessageService();
        username1 = "A";
        username2 = "C";
        message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("chat from A to C");
        res = EMS.addChatMessage(username1,username2, message);


    }

    
    @Test
    public void testloadWallMessages() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadWallMessages();
        
        assertEquals(list.size(),2);
        assertEquals(list.get(0).getContent(),"wall message from A");
        assertEquals(list.get(1).getContent(),"wall message from C");
    }


    
    @Test
    public void testloadVisibleWallMessages() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadVisibleWallMessages();

        assertEquals(list.size(),1);
        assertEquals(list.get(0).getContent(),"wall message from A");
    }

    @Test
    public void testloadChatMessages() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadChatMessages("A","B");

        assertEquals(list.get(0).getAuthor(),"A");
        assertEquals(list.get(0).getTarget(),"B");
        assertEquals(list.get(0).getContent(),"chat from A to B");
    }

    @Test
    public void testloadVisibleChatMessages() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadVisibleChatMessages("A","C");

        assertTrue(list.isEmpty());
    }

    @Test
    public void testloadAllAnnouncements() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadAllAnnouncements();

        assertEquals(list.size(),2);
        assertEquals(list.get(0).getContent(),"announcement message from A");
        assertEquals(list.get(1).getContent(),"announcement message from C");
    }

    @Test
    public void testloadVisibleAnnouncements() {

        ExchangeInfoService EIS = new ExchangeInfoService();
        List<ExchangeInfo> list;
        list = EIS.loadVisibleAnnouncements();

        assertEquals(list.size(),1);
        assertEquals(list.get(0).getContent(),"announcement message from A");
    }




    @AfterClass
    public static void clearTestData() throws Exception{

        /*Connection conn= getConnection();
        String dropTable = "DROP table SSN_USERS; DROP table SSN_MESSAGE";
        PreparedStatement stmtDrop = conn.prepareStatement(dropTable);
        stmtDrop.execute();
        PreparedStatement stmtCreateUsers = conn.prepareStatement(SQL.CREATE_USERS);
        stmtCreateUsers.execute();
        PreparedStatement stmtCreateChat = conn.prepareStatement(SQL.CREATE_CHAT);
        stmtCreateChat.execute();*/
        DBUtils.stopPerformanceRunning();
    }

}