package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignan on 10/8/14.
 */
@Path("/messages")
public class ExchangeInfoService extends BaseService{
    /**
     * GET service for getting all the Messages on Public Wall
     * @return publicWallMessages List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "wallMessages")
    @Path("/wall")
    public List<ExchangeInfo> loadWallMessages(){
        Log.enter();

        List<ExchangeInfo> wallMessages= null;
        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadWallMessages();

            wallMessages = new ArrayList<>();
            for(ExchangeInfoPO epo : eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                wallMessages.add(einfodto);
            }
        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(wallMessages);
        }
        return wallMessages;

    }
   /**
    * Tangent added, 10/30/2014
    */
    /**
     *GET service for getting all the Visible Messages on Public Wall
     * @return publicWallMessages(visible) List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "visibleWallMessages")
    @Path("/wall/visible")
    public List<ExchangeInfo> loadVisibleWallMessages(){
        Log.enter();

        List<ExchangeInfo> wallVisibleMessages= null;
        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadVisibleWallMessages();

            wallVisibleMessages = new ArrayList<>();
            for(ExchangeInfoPO epo : eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                wallVisibleMessages.add(einfodto);
            }
        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(wallVisibleMessages);
        }
        return wallVisibleMessages;

    }

    /**
     * GET service for getting all the private chat Messages between two users
     * @param userName1
     * @param userName2
     * @return privateChatMessages List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "chatMessages")
    @Path("/{userName1}/{userName2}")
    public List<ExchangeInfo> loadChatMessages(@PathParam("userName1") String userName1, @PathParam("userName2") String userName2){
        Log.enter();

        List<ExchangeInfo> chatMessages = null;
        UserPO po1 = loadExistingUser(userName1);
        UserPO po2 = loadExistingUser(userName2);


        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadChatMessages(po1, po2);

            chatMessages = new ArrayList<>();
            for (ExchangeInfoPO epo: eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                chatMessages.add(einfodto);
            }

        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(chatMessages);
        }

        return chatMessages;
    }

    /**
     * Get service for all the announcements
     * @return Announcements List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "announcements")
    @Path("/announcement")
    public List<ExchangeInfo> loadAllAnnouncements(){
        Log.enter();

        List<ExchangeInfo> announcements= null;
        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadAllAnnouncements();

            announcements = new ArrayList<>();
            for(ExchangeInfoPO epo : eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                announcements.add(einfodto);
            }
        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(announcements);
        }
        return announcements;

    }

    /**
     * Get service for all the visible announcements
     * @return Announcements(visible) List
     * @return Announcements(visible) List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "visibleAnnouncements")
    @Path("/announcement/visible")
    public List<ExchangeInfo> loadVisibleAnnouncements(){
        Log.enter();

        List<ExchangeInfo> visibleAnnouncements= null;
        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadVisibleAnnouncements();

            visibleAnnouncements = new ArrayList<>();
            for(ExchangeInfoPO epo : eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                visibleAnnouncements.add(einfodto);
            }
        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(visibleAnnouncements);
        }
        return visibleAnnouncements;

    }
    /**
     * GET service for getting all the private visible chat Messages between two users
     * @param userName1
     * @param userName2
     * @return privateChatMessages(visible) List
     */
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @XmlElementWrapper(name = "visibleChatMessages")
    @Path("/{userName1}/{userName2}/visible")
    public List<ExchangeInfo> loadVisibleChatMessages(@PathParam("userName1") String userName1, @PathParam("userName2") String userName2){
        Log.enter();

        List<ExchangeInfo> visibleChatMessages = null;
        UserPO po1 = loadExistingUser(userName1);
        UserPO po2 = loadExistingUser(userName2);


        try{
            List<ExchangeInfoPO> eInfoPO = DAOFactory.getInstance().getMessageDAO().loadVisibleChatMessages(po1, po2);

            visibleChatMessages = new ArrayList<>();
            for (ExchangeInfoPO epo: eInfoPO){
                ExchangeInfo einfodto = ConverterUtils.convert(epo);
                visibleChatMessages.add(einfodto);
            }

        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit(visibleChatMessages);
        }

        return visibleChatMessages;
    }

}
