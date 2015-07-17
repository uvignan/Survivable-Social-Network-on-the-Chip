package edu.cmu.sv.ws.ssnoc.rest;
/**
 * Created by vignan on 10/8/14.
 */
import com.sun.management.OperatingSystemMXBean;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMessageDAO;
import edu.cmu.sv.ws.ssnoc.data.dao.IUserDAO;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.lang.management.ManagementFactory;


@Path("/message")
public class ExchangeMessageService extends BaseService {
    /**
     * Exchange Information on to Wall.
     *
     * @param userName
     *            - User Name
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{userName}")
    public Response addWallMessage(@PathParam("userName") String userName, ExchangeInfo message ){
        Log.enter(userName, message);
        final int memoryCheckValue = 2048;//in kb
        String returnMessage = null;
        ExchangeInfo resp = new ExchangeInfo();
        try {
            UserPO po = null;

            if(DBUtils.isPerformaceRunning()){
                UserPO performancePO = new UserPO();
                performancePO.setUserName(userName);
               performancePO.setUserId(1);

                IMessageDAO mdao = DAOFactory.getInstance().getMessageDAO();

                ExchangeInfoPO einfopo = ConverterUtils.convert(message);

                Log.trace("Inserting message on public wall from.....:"+userName);

                mdao.saveWallMessage(performancePO, einfopo);
                resp = ConverterUtils.convert(einfopo);
            }else{
                IUserDAO udao = DAOFactory.getInstance().getUserDAO();
                po = udao.findByName(userName);

                IMessageDAO mdao = DAOFactory.getInstance().getMessageDAO();

                ExchangeInfoPO einfopo = ConverterUtils.convert(message);

                Log.trace("Inserting message on public wall from.....:"+userName);

                mdao.saveWallMessage(po, einfopo);
                resp = ConverterUtils.convert(einfopo);
            }
        }
        catch (Exception e){
            handleException(e);
        }
        finally {
            Log.exit();
        }
        Log.trace("Checking Memory Space after the message insertion");

        OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        long freeVMemory = bean.getFreePhysicalMemorySize()/1024;

        if(freeVMemory<memoryCheckValue)
        {
            Log.trace("freeVMemory working");
            returnMessage="Free Memory<2MB";
          //  return ok("Free Memory<2MB");
        }else{
            returnMessage="wall message saved";
           // return ok("wall message saved");
        }

        return ok(returnMessage);
    }

    /**
     *Exchange Information service to add the Private chat message to the database
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/{sendingUserName}/{receivingUserName}")
    public Response addChatMessage(@PathParam("sendingUserName") String sendingUserName, @PathParam("receivingUserName") String receivingUserName,ExchangeInfo message ){
    Log.enter(sendingUserName,receivingUserName,message);
        ExchangeInfo resp = new ExchangeInfo();
        try {
            UserPO po1 = loadExistingUser(sendingUserName);
            UserPO po2 = loadExistingUser(receivingUserName);

            IMessageDAO mdao = DAOFactory.getInstance().getMessageDAO();

            ExchangeInfoPO einfopo = ConverterUtils.convert(message);

            Log.trace("Inserting chat message from.....:"+sendingUserName + "to.."+receivingUserName);

            mdao.saveChatMessage(po1,po2, einfopo);
            resp = ConverterUtils.convert(einfopo);
        }
        catch (Exception e){
            handleException(e);
        }
        finally {
            Log.exit();
        }

        return created(resp);
    }

    /**
     * Exchange information service to add announcement to database
     * @param userName
     * @param message
     * @return ok(announcement saved)
     */
    @POST
    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    @Path("/announcement/{userName}")
    public Response addAnnouncement(@PathParam("userName") String userName, ExchangeInfo message){
        Log.enter(userName, message);

        //ExchangeInfo resp = new ExchangeInfo();
        try {
            UserPO po = loadExistingUser(userName);
            IMessageDAO mdao = DAOFactory.getInstance().getMessageDAO();

            ExchangeInfoPO einfopo = ConverterUtils.convert(message);

            Log.trace("Inserting announcement from.....:"+userName);

            mdao.saveAnnouncement(po, einfopo);
            //resp = ConverterUtils.convert(einfopo);
        }catch (Exception e ){
            handleException(e);
        }finally {
            Log.exit();
        }
        return ok("Announcement saved");
    }


}
