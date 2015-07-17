package edu.cmu.sv.ws.ssnoc.data.dao;

import java.util.List;

import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User information in the system.
 *
 * Added loadChatBuddiesByTime by YHWH on 10/13/14.
 */
public interface IUserDAO {
	/**
	 * This method will save the information of the user into the database.
	 * 
	 * @param userPO
	 *            - User information to be saved.
	 */
	void save(UserPO userPO);
    /**
     * This method will save the information of the user status into the database.
     *
     * @param statusPO
     *            - User information to be saved.
     */
    void saveStatus(UserPO userPO,StatusPO statusPO);
    /**
     * This method will save the status code information of the user into the SSN_USERS table in database.
     *
     * @param statusPO
     *            - User information to be saved.
     */
    void loadLastStatusCode(UserPO userPO, StatusPO statusPO);
    /**
	 * This method will load all the users in the
	 * database.
	 * 
	 * @return - List of all users.
	 */
    List<UserPO> loadUsers();

    /**
     * This method will load all the user statuses in the
     * database.
     *
     * @return - List of all user statuses.
     */
    List<StatusPO> loadStatuses(long user_id);


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
	UserPO findByName(String userName);

    /**
     * This method with search for a user status by unique crumbID in the database. The
     * search performed is a case insensitive search to allow case mismatch
     * situations.
     *
     * @param crumbID
     *            - User name to search for.
     *
     * @return - StatusPO with the user information if a match is found.
    */
    StatusPO findByCrumbID(long crumbID);

    /**
     * This method will search for all pairs of users who chatted with each other in
     * specific time period in the database. The search performed is a case insensitive
     * search to allow case mismatch situation.
     * @param startTime, endTime;
     *            - specific time period to search for.
     * @return - all chat pairs of UserPO.
     */
    List<List<UserPO>> loadChatBuddiesByTime(String startTime, String endTime);

    /**
     * This method will update the information of the user profile into the database.
     * @param userPO;
     *            - User information to be updated.
     */
    void updateUserProfile(UserPO oldUserDetail,UserPO userPO);

    /**
     * This method with search for a user by his userId in the database. The
     * search performed is a case insensitive search to allow case mismatch
     * situations.
     * @param userId;
     *            - User ID to search for.
     */
    UserPO findByUserID(long userId);

    /**
     * This method will load all active users in the
     * database.
     *
     * @return - List of all active users.
     */
    public List<UserPO> loadActiveUsers();


}
