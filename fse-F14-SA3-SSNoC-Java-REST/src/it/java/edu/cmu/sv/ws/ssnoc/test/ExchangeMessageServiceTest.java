package edu.cmu.sv.ws.ssnoc.test;

import static org.junit.Assert.*;

import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;
import javax.ws.rs.core.Response;
import edu.cmu.sv.ws.ssnoc.rest.ExchangeMessageService;

import org.junit.*;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class ExchangeMessageServiceTest {

    @BeforeClass
    public static void setUpTestData() throws Exception{

        DBUtils.setPerformaceRunning();
        Connection conn= DBUtils.getConnection();

        PreparedStatement stmtInsertUser = conn.prepareStatement(SQL.INSERT_USER);
        stmtInsertUser.setString(1, "A");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        stmtInsertUser.setString(1, "B");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();


    }



    @Test
    public void testAddWallMessage() {
        ExchangeMessageService EMS = new ExchangeMessageService();
        String username = "A";
        ExchangeInfo message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("Wallfortest");
        Response res = EMS.addWallMessage(username, message);

        assertEquals(res.getStatus(), 200);
        assertEquals(res.getEntity(), "wall message saved");
    }



    @Test
    public void testAddChatMessage() {
        ExchangeMessageService EMS = new ExchangeMessageService();
        String username = "A";
        String username2 = "B";
        ExchangeInfo message = new ExchangeInfo();
        message.setAuthor("A");
        message.setTarget("B");
        message.setContent("nice2meetUFrom1");
        Response res = EMS.addChatMessage(username, username2, message);

        assertEquals(res.getStatus(), 201);

    }

    @Test
    public void testAddAnnouncement() {
        ExchangeMessageService EMS = new ExchangeMessageService();
        String username = "A";
        ExchangeInfo message = new ExchangeInfo();
        message.setAuthor("A");
        message.setContent("Announcementfortest");
        Response res = EMS.addWallMessage(username, message);

        assertEquals(res.getStatus(), 200);
        assertEquals(res.getEntity(), "wall message saved");

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