package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;
import edu.cmu.sv.ws.ssnoc.dto.User;

@Path("/users")
public class UsersService extends BaseService {
	/**
	 * This method loads all users in the system.
	 * 
	 * @return - List of all users.
	 */
	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@XmlElementWrapper(name = "users")
    @Path("/")
	public List<User> loadUsers() {
		Log.enter();

		List<User> users = null;
		try {
			List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
            System.out.println(userPOs);

			users = new ArrayList<User>();
			for (UserPO po : userPOs) {
				User dto = ConverterUtils.convert(po);
				users.add(dto);
			}
		} catch (Exception e) {
			handleException(e);
		} finally {
			Log.exit(users);
		}

		return users;
	}

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @XmlElementWrapper(name="chatBuddies")
    @Path("/{userName}/chatbuddies")
    public List<ExchangeInfo> loadChatBuddies(@PathParam("userName") String userName){
        Log.enter();

        List<ExchangeInfo> buddies = null;
        UserPO po = loadExistingUser(userName);
        try{
            List<ExchangeInfoPO> buddiesPOs = DAOFactory.getInstance().getMessageDAO().loadChatBuddies(po);

            buddies = new ArrayList<ExchangeInfo>();
            for (ExchangeInfoPO exchangeInfoPO : buddiesPOs ){
                ExchangeInfo dto = ConverterUtils.convert(exchangeInfoPO);
                buddies.add(dto);
            }

        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(buddies);
        }
        return buddies;
    }

    /**
     * This method loads all active users in the system.
     *
     * @return - List of all active users.
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "activeUsers")
    @Path("/active")
    public List<User> loadActiveUsers(){
        Log.enter();

        List<User> activeUsers = null;
        try {
            List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadActiveUsers();

            activeUsers = new ArrayList<User>();
            for (UserPO po : userPOs) {
                User dto = ConverterUtils.convert(po);
                activeUsers.add(dto);
            }
        } catch (Exception e) {
            handleException(e);
        } finally {
            Log.exit(activeUsers);
        }

        return activeUsers;
    }

}
