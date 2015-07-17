package edu.cmu.sv.ws.ssnoc.test;

import edu.cmu.sv.ws.ssnoc.data.SQL;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.rest.SocialNetworkAnalysis;

import org.junit.*;

import javax.ws.rs.core.Response;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


/**
 * Created by YHWH on 10/10/14.
 */
public class SocialNetworkAnalysisTest {

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
        stmtInsertUser.setString(1, "C");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        stmtInsertUser.setString(1, "D");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();
        stmtInsertUser.setString(1, "E");
        stmtInsertUser.setString(2, null);
        stmtInsertUser.setString(3, null);
        stmtInsertUser.setString(4, null);
        stmtInsertUser.setString(5, null);
        stmtInsertUser.setString(6, null);
        stmtInsertUser.execute();

        PreparedStatement stmtInsertChat = conn.prepareStatement(SQL.INSERT_CHAT);
        stmtInsertChat.setString(1, "4");
        stmtInsertChat.setString(2, "CHAT");
        stmtInsertChat.setString(3, "5");
        stmtInsertChat.setString(4, "2014-10-01 14:33");
        stmtInsertChat.setString(5, "test");
        stmtInsertChat.execute();
        stmtInsertChat.setString(1, "2");
        stmtInsertChat.setString(2, "CHAT");
        stmtInsertChat.setString(3, "4");
        stmtInsertChat.setString(4, "2014-10-05 14:33");
        stmtInsertChat.setString(5, "test");
        stmtInsertChat.execute();
        stmtInsertChat.setString(1, "1");
        stmtInsertChat.setString(2, "CHAT");
        stmtInsertChat.setString(3, "3");
        stmtInsertChat.setString(4, "2014-10-10 14:33");
        stmtInsertChat.setString(5, "test");
        stmtInsertChat.execute();
        stmtInsertChat.setString(1, "3");
        stmtInsertChat.setString(2, "CHAT");
        stmtInsertChat.setString(3, "1");
        stmtInsertChat.setString(4, "2014-10-15 14:33");
        stmtInsertChat.setString(5, "test");
        stmtInsertChat.execute();

    }

    @Test
    public void loadAllUsersTest() {
        SocialNetworkAnalysis analysisTest = new SocialNetworkAnalysis();
        List<String> testData = new ArrayList<String>();
        String data1 = "A";
        String data2 = "B";
        String data3 = "C";
        String data4 = "D";
        String data5 = "E";

        testData.add(data1);
        testData.add(data2);
        testData.add(data3);
        testData.add(data4);
        testData.add(data5);

        assertEquals(testData, analysisTest.loadAllUsers());

    }

    @Test
    public void loadChatBuddiesTest() {
        SocialNetworkAnalysis analysisTest = new SocialNetworkAnalysis();
        String startTime = "2014-10-01 00:00";
        String endTime = "2014-10-31 23:59";

        List<List<String>> testData = new ArrayList<List<String>>();
        String userA = "A";
        String userB = "B";
        String userC = "C";
        String userD = "D";
        String userE = "E";

        List<String> data1 = new ArrayList<String>();
        data1.add(userD);
        data1.add(userE);
        List<String> data2 = new ArrayList<String>();
        data2.add(userB);
        data2.add(userD);
        List<String> data3 = new ArrayList<String>();
        data3.add(userA);
        data3.add(userC);

        testData.add(data1);
        testData.add(data2);
        testData.add(data3);

        List<List<String>> result = analysisTest.loadChatBuddies(startTime, endTime);

        assertEquals(testData, result);

    }

    @Test
    //no available chat
    public void loadNoChatBuddiesTest() {
        SocialNetworkAnalysis analysisTest = new SocialNetworkAnalysis();
        //UserDAOImpl loadTest = new UserDAOImpl();
        String startTime = "2014-09-01 00:00";
        String endTime = "2014-09-31 23:59";
        List<List<String>> result = analysisTest.loadChatBuddies(startTime, endTime);
        //List<List<UserPO>> result = loadTest.loadChatBuddiesByTime(startTime, endTime);

        assertTrue(result.isEmpty());
        assertNull(analysisTest.analyzeSocialNetwork(startTime, endTime));
    }

    @Test
    //only one available chat
    public void loadOneChatBuddiesTest() {
        SocialNetworkAnalysis analysisTest = new SocialNetworkAnalysis();

        //UserDAOImpl loadTest = new UserDAOImpl();
        String startTime = "2014-09-01 00:00";
        String endTime = "2014-10-01 23:59";
        List<List<String>> result = analysisTest.loadChatBuddies(startTime, endTime);
        //List<List<UserPO>> result = loadTest.loadChatBuddiesByTime(startTime, endTime);

        assertEquals(1, result.size());
    }

    @Test
    public void analyzeSocialNetworkTest() {
        SocialNetworkAnalysis analysisTest = new SocialNetworkAnalysis();
        String startTime = "1900-10-01 00:5";
        String endTime = "2014-10-31 23:5";

        List<List<String>> clusters = new ArrayList<List<String>>();

        String userA = "A";
        String userB = "B";
        String userC = "C";
        String userD = "D";
        String userE = "E";

        List<String> cluster1 = new ArrayList<String>();
        cluster1.add(userA);
        cluster1.add(userB);
        List<String> cluster2 = new ArrayList<String>();
        cluster2.add(userB);
        cluster2.add(userC);
        List<String> cluster3 = new ArrayList<String>();
        cluster3.add(userA);
        cluster3.add(userD);
        List<String> cluster4 = new ArrayList<String>();
        cluster4.add(userC);
        cluster4.add(userD);
        List<String> cluster5 = new ArrayList<String>();
        cluster5.add(userA);
        cluster5.add(userB);
        cluster5.add(userE);
        List<String> cluster6 = new ArrayList<String>();
        cluster6.add(userB);
        cluster6.add(userC);
        cluster6.add(userE);

        clusters.add(cluster6);
        clusters.add(cluster5);
        clusters.add(cluster4);
        clusters.add(cluster3);
        clusters.add(cluster2);
        clusters.add(cluster1);

        Response result = analysisTest.analyzeSocialNetwork(startTime, endTime);
        String res = result.getEntity().toString();

        String ans = "[";
        for(int i= 0; i < clusters.size();i++){
            ans += "[";
            for(int j=0; j < clusters.get(i).size(); j++){
                ans += "\"";
                ans += clusters.get(i).get(j);
                ans += "\",";
            }
            ans = ans.substring(0,ans.length()-1);
            ans += "],";
        }
        ans = ans.substring(0,ans.length()-1);
        ans += "]";

        //assertTrue(clusters.containsAll(result) && result.containsAll(clusters));
        assertEquals(ans, res);
    }

    @AfterClass
    public static void clearTestData() throws Exception{

        DBUtils.stopPerformanceRunning();
    }

}