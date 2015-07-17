package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by vignan on 10/8/14.
 *
 */
public class MessageDAOImpl extends BaseDAOImpl implements IMessageDAO{

    @Override
    public List<ExchangeInfoPO> loadWallMessages(){
        Log.enter();

        String query = SQL.FIND_ALL_WALL_MESSAGES;

        List<ExchangeInfoPO> wallMessages = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            wallMessages = processWallMessages(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(wallMessages);
        }
        return wallMessages;
    }

    private List<ExchangeInfoPO> processWallMessages(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside process wall messages method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<ExchangeInfoPO> wallMessages = new ArrayList<ExchangeInfoPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ExchangeInfoPO epo = new ExchangeInfoPO();
                epo.setAuthor(rs.getString(1));
                epo.setContent(rs.getString(2));
                epo.setPostedAt(rs.getString(3));
                epo.setImgPath(rs.getString(4));
                epo.setLatitude(rs.getFloat(5));
                epo.setLongitude(rs.getFloat(6));

                wallMessages.add(epo);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(wallMessages);
        }
        return wallMessages;
    }


    @Override
    public void saveWallMessage(UserPO userPO, ExchangeInfoPO einfoPO){
        Log.enter(einfoPO);
        if (einfoPO == null) {
            Log.warn("Inside save method for wall message with einfoPO == NULL");
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_CHAT)) {
            stmt.setLong(1, userPO.getUserId());
            stmt.setString(2, "Wall");
            stmt.setString(3,null);
            stmt.setString(4,df.format(dateobj));
            stmt.setString(5, einfoPO.getContent());
            stmt.setString(6, einfoPO.getImgPath());
            stmt.setFloat(7, einfoPO.getLatitude());
            stmt.setFloat(8, einfoPO.getLongitude());

            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

    @Override
    public List<ExchangeInfoPO> loadChatMessages(UserPO po1, UserPO po2){
        Log.enter();

        List<ExchangeInfoPO> chatMessages = new ArrayList<>();
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement(SQL.FIND_CHAT_MESSAGES)) {
            stmt.setLong(1, po1.getUserId());
            stmt.setLong(2, po2.getUserId());
            stmt.setLong(3, po1.getUserId());
            stmt.setLong(4, po2.getUserId());
            chatMessages = processChatMessages(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(chatMessages);
        }
        return chatMessages;
    }

    private List<ExchangeInfoPO> processChatMessages(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside process chat messages method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<ExchangeInfoPO> chatMessages = new ArrayList<ExchangeInfoPO>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ExchangeInfoPO eipo = new ExchangeInfoPO();
                eipo.setAuthor(rs.getString(1));
                eipo.setTarget(rs.getString(2));
                eipo.setContent(rs.getString(3));
                eipo.setPostedAt(rs.getString(4));

                chatMessages.add(eipo);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(chatMessages);
        }
        return chatMessages;
    }

    @Override
    public void saveChatMessage(UserPO userPO1,UserPO userPO2, ExchangeInfoPO einfoPO){
        Log.enter(einfoPO);
        if (einfoPO == null) {
            Log.warn("Inside save method for wall message with einfoPO == NULL");
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_CHAT)) {
            stmt.setLong(1, userPO1.getUserId());
            stmt.setString(2, "Chat");
            stmt.setLong(3,userPO2.getUserId());
            stmt.setString(4,df.format(dateobj));
            stmt.setString(5, einfoPO.getContent());

            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

    @Override
    public List<ExchangeInfoPO> loadChatBuddies(UserPO po){
        Log.enter();

        List<ExchangeInfoPO> chatBuddies = new ArrayList<>();
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement(SQL.FIND_CHAT_BUDDIES)) {
            stmt.setLong(1, po.getUserId());
            chatBuddies = processChatBuddies(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(chatBuddies);
        }
        return chatBuddies;
    }

    private List<ExchangeInfoPO> processChatBuddies(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processStatuses method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<ExchangeInfoPO> chatBuddies = new ArrayList<ExchangeInfoPO>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ExchangeInfoPO eipo = new ExchangeInfoPO();
                eipo.setTarget(rs.getString(1));
                chatBuddies.add(eipo);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(chatBuddies);
        }
        return chatBuddies;
    }

    @Override
    public void saveAnnouncement(UserPO userPO, ExchangeInfoPO einfoPO){
        Log.enter(einfoPO);
        if (einfoPO == null) {
            Log.warn("Inside save method for wall message with einfoPO == NULL");
            return;
        }
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_CHAT)) {
            stmt.setLong(1, userPO.getUserId());
            stmt.setString(2, "Announcement");
            stmt.setString(3,null);
            stmt.setString(4,df.format(dateobj));
            stmt.setString(5, einfoPO.getContent());

            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }

    @Override
    public List<ExchangeInfoPO> loadAllAnnouncements(){
        Log.enter();

        String query = SQL.FIND_ALL_ANNOUNCEMENTS;

        List<ExchangeInfoPO> announcements = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            announcements = processAllAnnouncements(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(announcements);
        }
        return announcements;
    }

    private List<ExchangeInfoPO> processAllAnnouncements(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside process announcements method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<ExchangeInfoPO> announcements = new ArrayList<ExchangeInfoPO>();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                ExchangeInfoPO epo = new ExchangeInfoPO();
                epo.setAuthor(rs.getString(1));
                epo.setContent(rs.getString(2));
                epo.setPostedAt(rs.getString(3));

                announcements.add(epo);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(announcements);
        }
        return announcements;
    }

    @Override
    public List<ExchangeInfoPO> loadVisibleWallMessages(){
        Log.enter();

        String query = SQL.FIND_VISIBLE_WALL_MESSAGES;

        List<ExchangeInfoPO> wallMessages = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            wallMessages = processWallMessages(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(wallMessages);
        }
        return wallMessages;
    }

    @Override
    public List<ExchangeInfoPO> loadVisibleAnnouncements(){
        Log.enter();

        String query = SQL.FIND_VISIBLE_ANNOUNCEMENTS;

        List<ExchangeInfoPO> announcements = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            announcements = processAllAnnouncements(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(announcements);
        }
        return announcements;
    }

    @Override
    public List<ExchangeInfoPO> loadVisibleChatMessages(UserPO po1, UserPO po2){
        Log.enter();

        List<ExchangeInfoPO> visibleChatMessages = new ArrayList<>();
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement(SQL.FIND_VISIBLE_CHAT_MESSAGES)) {
            stmt.setLong(1, po1.getUserId());
            stmt.setLong(2, po2.getUserId());
            stmt.setLong(3, po1.getUserId());
            stmt.setLong(4, po2.getUserId());
            visibleChatMessages = processChatMessages(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(visibleChatMessages);
        }
        return visibleChatMessages;
    }

}
