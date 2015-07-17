package edu.cmu.sv.ws.ssnoc.data.po;

import com.google.gson.Gson;

/**
 * This is the persistence class to save all user information in the system.
 * This contains information like the user's name, his role, his account status
 * and the password information entered by the user when signing up. <br/>
 * Information is saved in SSN_USERS table.
 * 
 */
public class UserPO {
	private long userId;
	private String userName;
	private String password;
    private String statusCode;
    private String statusDate;
    private String salt;
    private String accountStatus;
    private String privilegeLevel;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public void setStatusCode(String statusCode){this.statusCode = statusCode;}

    public String getStatusCode() {return statusCode;}

    public String getStatusDate() {return statusDate;}

    public void  setStatusDate(String statusDate){this.statusDate = statusDate;}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

    public String getAccountStatus() { return accountStatus; }

    public void setAccountStatus(String accountStatus) { this.accountStatus = accountStatus;}

    public String getPrivilegeLevel() { return privilegeLevel;}

    public void setPrivilegeLevel(String privilegeLevel) { this.privilegeLevel = privilegeLevel; }

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}

}
