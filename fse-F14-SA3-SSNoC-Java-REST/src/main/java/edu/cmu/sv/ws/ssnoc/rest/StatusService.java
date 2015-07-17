package edu.cmu.sv.ws.ssnoc.rest;

/**
 * Created by vignan on 9/29/14.
 */

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Status;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * This class contains the implementation of the RESTful API calls made with
 * respect to users.
 *
 */
@Path("/status")
public class StatusService extends BaseService{
    /**
     * All status information related to a particular userName.
     *
     * @param userName
     *            - User Name
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/{userName}")
   public void updateStatus(@PathParam("userName") String userName, Status userStatus) {
        Log.enter(userName, userStatus);
        try {
            UserPO po = loadExistingUser(userName);
            IUserDAO dao = DAOFactory.getInstance().getUserDAO();
            StatusPO spo = ConverterUtils.convert(userStatus);
            Log.trace("Inserting status details.....:"+userName);
            dao.saveStatus(po,spo);
            dao.loadLastStatusCode(po,spo);
            //Status update on Wall Message
            ExchangeInfoPO msg = new ExchangeInfoPO();
            String content = null;
            switch (spo.getStatusCode()){
                case "OK":
                    content = "I am OK, I do not need help";
                    break;
                case "HELP":
                    content = "I need help, but this is not a life threatening emergency";
                    break;
                case "EMERGENCY":
                    content = "I need help now, as this is a life threatening emergency!";
                    break;
                default:
                    content = "The user has not been providing his/her status yet";
            }
            msg.setContent(content);

            IMessageDAO mdao = DAOFactory.getInstance().getMessageDAO();
            mdao.saveWallMessage(po,msg);
        }
        catch (Exception e){
            handleException(e);
        }
        finally {
            Log.exit();
        }
    }

    @GET
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{crumbID}")
    public Status loadWithCrumbID(@PathParam("crumbID") long crumbID){
        Log.enter(crumbID);
        Status sto = null;
        try{
            StatusPO spo = DAOFactory.getInstance().getUserDAO().findByCrumbID(crumbID);
            sto = ConverterUtils.convert(spo);
        }
        catch(Exception e){
            handleException(e);
        }
        finally{
            Log.exit();
        }
        return sto;
    }

}
