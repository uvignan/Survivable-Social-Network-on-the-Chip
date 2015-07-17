package edu.cmu.sv.ws.ssnoc.data;
/**
 * This class contains all the SQL related code that is used by the project.
 * Note that queries are grouped by their purpose and table associations for
 * easy maintenance.
 * 
 */
public class SQL {    /*
     * List the USERS table name, and list all queries related to this table
     * here.
     */
    public static final String SSN_USERS = "SSN_USERS";
    public static final String SSN_STATUS_CRUMB="SSN_STATUS";
    public static final String SSN_CHAT="SSN_MESSAGE";
    public static final String SSN_MEMORY_CRUMB="SSN_MEMORY";
    /**
     * Query to check if a given table exists in the H2 database.
     */
    public static final String CHECK_TABLE_EXISTS_IN_DB = "SELECT count(1) as rowCount "
            + " FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = SCHEMA() "
            + " AND UPPER(TABLE_NAME) = UPPER(?)";

    /**
     * Query to initialize an Administrator in DB.
     */
    public static final String CREATE_INITIAL_ADMINISTRATOR = "insert into " + SSN_USERS
            + " (user_name, password , created_date, salt, account_status, privilege_level) values ('SSNAdmin', ?, ?, ?, ?, ?)"; //Tangent edited, 10/30/2014

    /*****************************************************
     All queries related to CREATIONS
     *****************************************************/
    //USERS table
    public static final String CREATE_USERS = "create table IF NOT EXISTS "
            + SSN_USERS + " ( user_id IDENTITY PRIMARY KEY,"
            + " user_name VARCHAR(100)," + " password VARCHAR(512),"
            +" created_date VARCHAR(100)," + " modifiedAt VARCHAR(100)," +
            " last_status_code VARCHAR(50)," +
            " last_status_date VARCHAR(50),"
            + " salt VARCHAR(512),"
            + " account_status VARCHAR(15)," + " privilege_level VARCHAR(20) )"; //Tangent edited, 10/28/2014
    //STATUS table
    public static final String CREATE_STATUS_CRUMB = "create table IF NOT EXISTS "
            + SSN_STATUS_CRUMB +
            " (status_id IDENTITY PRIMARY KEY, " +
            "user_id long REFERENCES SSN_USERS(user_id), " +
            "status_code VARCHAR(15), " +
            "status_date VARCHAR(50))";
    //CHAT table
    public static final String CREATE_CHAT = "create table IF NOT EXISTS "
            + SSN_CHAT +" ( message_id IDENTITY PRIMARY KEY, " +
            "author_id long, " +
            "message_type VARCHAR(20), " +
            "target_id long, " +
            "postedAt VARCHAR(100), location varchar(100), content varchar(1000), img_path VARCHAR(100), " +
            "latitude float, longitude float)";
    //Memory table
    public static final String CREATE_MEMORY_CRUMB = "create table IF NOT EXISTS "
            + SSN_MEMORY_CRUMB +
            " ( memory_id IDENTITY PRIMARY KEY, usedVMemory long, remainingVMemory long,usedPersistent long, " +
            "remainingPersistent long, createdAt varchar(20))";

    /****************************************************
     All SELECT queries
     ****************************************************/
    //USERS table
    public static final String FIND_ALL_USERS = "select user_id, " +
            "user_name, password, last_status_code, last_status_date, "
            + " salt,"
            + " account_status," + " privilege_level"
            + " from "
            + SSN_USERS+
            " order by user_name";  //Tangent edited, 10/30/2014

    public static final String FIND_USER_BY_NAME = "select user_id, " +
            " user_name, password, last_status_code, last_status_date, "
            + " salt,"
            + " account_status," + " privilege_level"
            + " from "
            + SSN_USERS
            + " where UPPER(user_name) = UPPER(?)";  //Tangent edited, 10/30/2014

    public static final String FIND_USER_BY_ID = "select user_id, " +
            " user_name, password, last_status_code, last_status_date, "
            + " salt,"
            + " account_status," + " privilege_level"
            + " from "
            + SSN_USERS
            + " where UPPER(user_id) = UPPER(?)";  //Tangent added, 10/28/2014

    public static final String FIND_ACTIVE_USERS = "select user_id, " +
            "user_name, password, last_status_code, last_status_date, "
            + " salt,"
            + " account_status," + " privilege_level"
            + " from "
            + SSN_USERS
            + " where UPPER(account_status) = UPPER(?)"+
            " order by user_name";  //Tangent added, 10/30/2014

    //STATUS table
    public static final String FIND_STATUS_BY_CRUMB = "select status_id,user_name, status_code, status_date"
            +" from "
            + SSN_STATUS_CRUMB+","+SSN_USERS
            +" where "+SSN_STATUS_CRUMB+".user_id="+SSN_USERS+".user_id and "+
            "UPPER(status_id) = UPPER(?)";
    public static final String FIND_ALL_USER_STATUSES = "select status_id,user_name, status_code, status_date "
            + " from " + SSN_STATUS_CRUMB+","+SSN_USERS
            +" where " +SSN_STATUS_CRUMB+".user_id="+SSN_USERS+".user_id and "+
            "UPPER("+SSN_USERS+".user_id) = UPPER(?)"
            + " order by status_date";
    //CHAT table
    public static final String FIND_ALL_WALL_MESSAGES = "select u1.user_name, content, postedAt, img_path, latitude, longitude"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"
            +" where author_id=u1.user_id and "+
            "UPPER(message_type)='WALL' "
            +"order by postedAt";

    public static final String FIND_VISIBLE_WALL_MESSAGES = "select u1.user_name, content, postedAt, img_path, latitude, longitude"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"
            +" where author_id=u1.user_id and "+
            "UPPER(message_type)='WALL' and "+
            "UPPER(account_status)='1' "
            +"order by postedAt";  //Tangent added, 10/30/2014

    public static final String FIND_CHAT_MESSAGES = "select u1.user_name, u2.user_name, content, postedAt"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"+","+SSN_USERS+" u2"
            +" where author_id=u1.user_id and target_id=u2.user_id and " +
            "UPPER(message_type)='CHAT' "
            +"and ((UPPER(author_id) = UPPER(?) and UPPER(target_id) = UPPER(?)) OR (UPPER(target_id) = UPPER(?) and UPPER(author_id) = UPPER(?)))"
            +" order by postedAt";

    public static final String FIND_VISIBLE_CHAT_MESSAGES = "select u1.user_name, u2.user_name, content, postedAt"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"+","+SSN_USERS+" u2"
            +" where author_id=u1.user_id and target_id=u2.user_id and " +
            "UPPER(message_type)='CHAT' "
            +"and UPPER(u1.account_status)='1' and UPPER(u2.account_status)='1' "
            +"and ((UPPER(author_id) = UPPER(?) and UPPER(target_id) = UPPER(?)) OR (UPPER(target_id) = UPPER(?) and UPPER(author_id) = UPPER(?)))"
            +" order by postedAt";

    public static final String FIND_CHAT_BUDDIES = "select distinct user_name" +" from " + SSN_CHAT+","+SSN_USERS
            +" where target_id=user_id and UPPER(message_type)='CHAT' "
            +"and UPPER(author_id)=UPPER(?)";
    //Social Network Analysis
    public static final String FIND_CHAT_BUDDIES_BY_TIME_PERIOD = "select message_id, u1.user_name, u2.user_name" +" from " + SSN_CHAT + " a" +
            ","+SSN_USERS+" u1"+","+SSN_USERS+" u2"
            +" where author_id=u1.user_id and target_id=u2.user_id and  " +
            "a.message_id=(SELECT MIN(b.message_id)" +" from " + SSN_CHAT + " b"
            +" where least(a.author_id, a.target_id)= least(b.author_id, b.target_id) "
            +"and greatest(a.author_id, a.target_id)= greatest(b.author_id, b.target_id) "
            +"and UPPER(message_type)='CHAT' "
            +"and UPPER(postedAt) between UPPER(?) and UPPER(?))";
    //MEMORY table
    public static final String GET_MEMORY_STATS = "select usedVMemory, remainingVMemory, usedPersistent, remainingPersistent, " +
            "createdAt from "+SSN_MEMORY_CRUMB+" where createdAt between (?) and (?)"+" order by createdAt DESC";
    //ANNOUNCEMENTS table
    public static final String FIND_ALL_ANNOUNCEMENTS = "select u1.user_name, content, postedAt"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"
            +" where author_id=u1.user_id and "+
            "UPPER(message_type)='ANNOUNCEMENT' "
            +"order by postedAt";
    //ANNOUNCEMENTS table
    public static final String FIND_VISIBLE_ANNOUNCEMENTS = "select u1.user_name, content, postedAt"
            +" from "
            + SSN_CHAT+","+SSN_USERS+" u1"
            +" where author_id=u1.user_id and "+
            "UPPER(message_type)='ANNOUNCEMENT' and "+
            "UPPER(account_status)='1' "
            +"order by postedAt";

    /********************************************************
     All Insert queries
     ********************************************************/
    //USERS table
    public static final String INSERT_USER = "insert into " + SSN_USERS
            + " (user_name, password , created_date, salt, account_status, privilege_level) values (?, ?, ?, ?, ?, ?)"; //Tangent edited, 10/30/2014
    //STATUS table
    public static final String INSERT_STATUS = "insert into "+ SSN_STATUS_CRUMB
            +" (user_id,status_code,status_date) values (?,?,?)";
    //CHAT table
    public static final String INSERT_CHAT = "insert into "+ SSN_CHAT
            +" (author_id, message_type, target_id,postedAT,content,img_path, latitude, longitude) values (?,?,?,?,?,?,?,?)";
    //MEMORY table
    public static final String INSERT_MEMORY_STATS = "insert into "+ SSN_MEMORY_CRUMB
            +" (usedVMemory, remainingVMemory, usedPersistent,remainingPersistent,createdAt) values (?,?,?,?,?)";

    /*********************************************************
     UPDATE queries
     *********************************************************/
    //USERS table
    public static final String UPDATE_STATUS = "update "+SSN_USERS+
            " SET last_status_code = ? , last_status_date =? where UPPER(user_id) = UPPER(?)";

    public static final String UPDATE_USER_PROFILE = "update "+SSN_USERS+
            " SET user_name = ? , password = ? , modifiedAt = ? , salt = ? , account_status = ? , privilege_level = ?"
            +" where UPPER(user_id) = UPPER(?)"; //Tangent added, 10/28/2014

    /*******************************************************
     All DELETE/TRUNCATE
     *******************************************************/
    //MEMORY table
    public static final String DELETE_MEMORY_STATS = "Truncate table "+SSN_MEMORY_CRUMB;

    
    /************************************************************************
     * All Queries related to Search
     *************************************************************************/
    public static final String SEARCH_USER_BY_NAME = "select user_name"
			+ " from "
			+ SSN_USERS
			+ " where user_name = ?"
			+ " order by user_name ASC";
    
    public static final String SEARCH_USER_BY_STATUS = "select user_name"
			+ " from "
			+ SSN_USERS + " a "
			+ " inner join "
			+ SSN_STATUS_CRUMB + " b "
			+ " on a.user_id = b.user_id"
			+ " where status_code = UPPER(?)"
			+ " order by user_name ASC";
    public static final String SEARCH_ANNOUNCEMENT = "select user_name, postedAt, content"
			+ " from "
			+ SSN_USERS + " a "
			+ " inner join "
			+ SSN_CHAT + " b "
			+ " on a.user_id = b.author_id"
			+ " where UPPER(message_type)='ANNOUNCEMENT' "
			+ " and content LIKE ?"
			+ " order by postedAt DESC";
    public static final String SEARCH_PUBLIC_MESSAGE = "select user_name, postedAt, content"
			+ " from "
			+ SSN_USERS + " a "
			+ " inner join "
			+ SSN_CHAT + " b "
			+ " on a.user_id = b.author_id"
			+ " where UPPER(message_type)='WALL' "
			+ " and content LIKE ?"
			+ " order by postedAt DESC";
    public static final String SEARCH_PRIVATE_MESSAGE = "select user_name, postedAt, content"
			+ " from "
			+ SSN_USERS + " a "
			+ " inner join "
			+ SSN_CHAT + " b "
			+ " on a.user_id = b.author_id"
			+ " where UPPER(message_type)='CHAT' "
			+ " and content LIKE ?"
			+ " order by postedAt DESC";



}
