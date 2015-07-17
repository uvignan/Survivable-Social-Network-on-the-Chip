package edu.cmu.sv.ws.ssnoc.data.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.SQL;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * DAO implementation for saving User information in the H2 database.
 * Added loadChatBuddiesByTime, processChatBuddiesByTime by YHWH on 10/13/14.
 */
public class UserDAOImpl extends BaseDAOImpl implements IUserDAO {
	/**
	 * This method will load users from the DB with specified account status. If
	 * no status information (null) is provided, it will load all users.
	 * 
	 * @return - List of users
	 */
	public List<UserPO> loadUsers() {
		Log.enter();

		String query = SQL.FIND_ALL_USERS;

		List<UserPO> users = new ArrayList<UserPO>();
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(query);) {
			users = processResults(stmt);
		} catch (SQLException e) {
			handleException(e);
			Log.exit(users);
		}

		return users;

	}

	private List<UserPO> processResults(PreparedStatement stmt) {
		Log.enter(stmt);

		if (stmt == null) {
			Log.warn("Inside processResults method with NULL statement object.");
			return null;
		}

		Log.debug("Executing stmt = " + stmt);
		List<UserPO> users = new ArrayList<UserPO>();
		try (ResultSet rs = stmt.executeQuery()) {
			while (rs.next()) {
				UserPO po = new UserPO();
				po.setUserId(rs.getLong(1));
				po.setUserName(rs.getString(2));
				po.setPassword(rs.getString(3));
                po.setStatusCode(rs.getString(4));
                po.setStatusDate(rs.getString(5));
				po.setSalt(rs.getString(6));
                po.setAccountStatus(rs.getString(7));  //Tangent edited, 10/30/2014
                po.setPrivilegeLevel(rs.getString(8)); //Tangent edited, 10/30/2014

				users.add(po);
			}
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

	/**
	 * This method with search for a user by his userName in the database. The
	 * search performed is a case insensitive search to allow case mismatch
	 * situations.
	 * 
	 * @param userName
	 *            - User name to search for.
	 * 
	 * @return - UserPO with the user information if a match is found.
	 */
	@Override
	public UserPO findByName(String userName) {
		Log.enter(userName);

		if (userName == null) {
			Log.warn("Inside findByName method with NULL userName.");
			return null;
		}

		UserPO po = null;
		try (Connection conn = getConnection();
				PreparedStatement stmt = conn
						.prepareStatement(SQL.FIND_USER_BY_NAME)) {
			stmt.setString(1, userName.toUpperCase());

			List<UserPO> users = processResults(stmt);

			if (users.size() == 0) {
				Log.debug("No user account exists with userName = " + userName);
			} else {
				po = users.get(0);
			}
		} catch (SQLException e) {
			handleException(e);
			Log.exit(po);
		}

		return po;
	}

	/**
	 * This method will save the information of the user into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */
	@Override
	public void save(UserPO userPO) {
		Log.enter(userPO);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();
		if (userPO == null) {
			Log.warn("Inside save method with userPO == NULL");
			return;
		}

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_USER)) {
			stmt.setString(1, userPO.getUserName());
			stmt.setString(2, userPO.getPassword());
            stmt.setString(3,df.format(dateobj));
			stmt.setString(4, userPO.getSalt());
            stmt.setString(5, userPO.getAccountStatus());//accountStatus
            stmt.setString(6, userPO.getPrivilegeLevel());//privilegeLevel


            int rowCount = stmt.executeUpdate();
			Log.trace("Statement executed, and " + rowCount + " rows inserted.");
		} catch (SQLException e) {
			handleException(e);
		} finally {
			Log.exit();
		}
	}

    /**
     * This method will save the information of the user into the SSN_STATUS_CRUMB Table in database.
     *
     * @param   statusPO
     *            - User information to be saved.
     */
    @Override
    public void saveStatus(UserPO userPO,StatusPO statusPO){
        Log.enter(statusPO);

        if (statusPO == null) {
            Log.warn("Inside save method with userPO == NULL");
            return;
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.INSERT_STATUS)) {
            stmt.setLong(1, userPO.getUserId());
            stmt.setString(2, statusPO.getStatusCode());
            stmt.setString(3, statusPO.getCreatedDate());
            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }
    }
    /**
     * This method will update the latest status code information of the user into the SSN_USERS Table in database.
     *
     * @param   statusPO
     *            - User information to be saved.
     */
    @Override
    public void loadLastStatusCode(UserPO userPO,StatusPO statusPO){
        Log.enter(statusPO);

        if (statusPO == null) {
            Log.warn("Inside save method with userPO == NULL");
            return;
        }
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_STATUS)) {
             stmt.setString(1, statusPO.getStatusCode());
            stmt.setString(2,statusPO.getCreatedDate());
             stmt.setLong(3, userPO.getUserId());
            int rowCount = stmt.executeUpdate();
            conn.commit();
            Log.info("Status details added");
            Log.trace("Statement executed, and " + rowCount + " rows UPD.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }

    }


    public List<StatusPO> loadStatuses(long user_id) {
        Log.enter();

        List<StatusPO> statuses = new ArrayList<StatusPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.FIND_ALL_USER_STATUSES)) {
            stmt.setLong(1, user_id);
            statuses = processStatuses(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(statuses);
        }

        return statuses;
    }

    private List<StatusPO> processStatuses(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processStatuses method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        List<StatusPO> statuses = new ArrayList<StatusPO>();

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                StatusPO spo = new StatusPO();
                spo.setCrumbID(rs.getLong(1));
                spo.setUserName(rs.getString(2));
                spo.setStatusCode(rs.getString(3));
                spo.setCreatedDate(rs.getString(4));


                statuses.add(spo);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(statuses);
        }

        return statuses;
    }

    @Override
    public StatusPO findByCrumbID(long crumbID) {
        Log.enter(crumbID);

        if (crumbID == 0) {
            Log.warn("Inside findByCrumbID method with NULL crumbID.");
            return null;
        }

        StatusPO spo = new StatusPO();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.FIND_STATUS_BY_CRUMB)) {
            stmt.setLong(1, crumbID);

            spo = processCrumb(stmt);

            if (spo == null) {
                Log.debug("No user crumb exists with crumbID = " + crumbID);
            }
        } catch (SQLException e) {
            handleException(e);
            Log.exit(spo);
        }
        return spo;
    }



    private StatusPO processCrumb(PreparedStatement stmt) {
        Log.enter(stmt);

        if (stmt == null) {
            Log.warn("Inside processResults method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);
        StatusPO spo =  new StatusPO();
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                spo.setCrumbID(rs.getLong(1));
                spo.setUserName(rs.getString(2));
                spo.setStatusCode(rs.getString(3));
                spo.setCreatedDate(rs.getString(4));
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(spo);
        }
        return spo;

    }

    @Override
    public List<List<UserPO>> loadChatBuddiesByTime(String startTime, String endTime){
        Log.enter();
        List<List<UserPO>> chatBuddies = new ArrayList<List<UserPO>>();
        try(Connection conn= getConnection();
            PreparedStatement stmt = conn
                    .prepareStatement(SQL.FIND_CHAT_BUDDIES_BY_TIME_PERIOD)) {
            stmt.setString(1, startTime.toUpperCase());
            stmt.setString(2, endTime.toUpperCase());
            chatBuddies = processChatBuddiesByTime(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(chatBuddies);
        }
        return chatBuddies;
    }
    private List<List<UserPO>> processChatBuddiesByTime (PreparedStatement stmt) {
        Log.enter(stmt);
        if (stmt == null) {
            Log.warn("Inside processStatuses method with NULL statement object.");
            return null;
        }

        Log.debug("Executing stmt = " + stmt);

        List<List<UserPO>> chatBuddies = new ArrayList<List<UserPO>>();

        try (ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                List<UserPO> chatPair = new ArrayList<UserPO>();

                UserPO upo1 = new UserPO();
                UserPO upo2 = new UserPO();
                upo1.setUserName(rs.getString(2));
                upo2.setUserName(rs.getString(3));
                chatPair.add(upo1);
                chatPair.add(upo2);
                chatBuddies.add(chatPair);
            }
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit(chatBuddies);
        }
        return chatBuddies;
    }

    @Override
    public void updateUserProfile (UserPO oldUserDetail, UserPO userPO) {
        Log.enter(userPO);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateobj = new Date();
        if (userPO == null) {
            Log.warn("Inside updateUserProfile method with userPO == NULL");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL.UPDATE_USER_PROFILE)) {
            stmt.setString(1, userPO.getUserName());
            stmt.setString(2, userPO.getPassword());
            stmt.setString(3,df.format(dateobj)); //modified date
            stmt.setString(4, userPO.getSalt());
            stmt.setString(5, userPO.getAccountStatus());
            stmt.setString(6, userPO.getPrivilegeLevel());
            stmt.setLong(7, oldUserDetail.getUserId());


            int rowCount = stmt.executeUpdate();
            Log.trace("Statement executed, and " + rowCount + " rows inserted.");
        } catch (SQLException e) {
            handleException(e);
        } finally {
            Log.exit();
        }

    }

    @Override
    public UserPO findByUserID(long userId) {
        Log.enter(userId);

        if (userId == 0) {
            Log.warn("Inside findByUserID method with NULL userId.");
            return null;
        }

        UserPO po = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn
                     .prepareStatement(SQL.FIND_USER_BY_ID)) {
            stmt.setLong(1, userId);

            List<UserPO> users = processResults(stmt);


            if (users.size() == 0) {
                Log.debug("No user account exists with userId = " + userId);
            } else {
                po = users.get(0);
            }
        } catch (SQLException e) {
            handleException(e);
            Log.exit(po);
        }

        return po;
    }

    public List<UserPO> loadActiveUsers() {
        Log.enter();

        String query = SQL.FIND_ACTIVE_USERS;

        List<UserPO> activeUsers = new ArrayList<UserPO>();
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);) {
            stmt.setString(1, "1".toUpperCase()); //1 for active, 0 for inactive
            activeUsers = processResults(stmt);
        } catch (SQLException e) {
            handleException(e);
            Log.exit(activeUsers);
        }

        return activeUsers;

    }

}
