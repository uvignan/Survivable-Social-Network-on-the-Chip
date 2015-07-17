package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;


import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.User;

/**
 * created by Tangent on Oct.6 2014
 * 
 */

@Path("/usergroups")
public class  SocialNetworkAnalysis extends BaseService{
	/**
	 * This method analyze the social network of specific time period.
	 * @return - clusters list
	 */
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
    @Path("/unconnected/{startTime}/{endTime}")
    public Response analyzeSocialNetwork(@PathParam("startTime") String startTime, @PathParam("endTime") String endTime){
            Log.enter();
        List<List<String>> clusters = new ArrayList<List<String>>();
        List<String> allUsers = loadAllUsers();
        List<List<String>> buddies = loadChatBuddies(startTime, endTime);

        try {
            clusters.add(allUsers);

            if(buddies.isEmpty()){ return null;}
            else {
                for (List<String> eachPair : buddies) {
                    List<List<String>> temp = new ArrayList<List<String>>();
                    for (List<String> eachCluster : clusters) {
                        if (eachCluster.containsAll(eachPair)) {
                            List<String> cluster1 = new ArrayList<String>(eachCluster);
                            List<String> cluster2 = new ArrayList<String>(eachCluster);
                            cluster1.remove(eachPair.get(0));
                            cluster2.remove(eachPair.get(1));
                            temp.add(cluster1);
                            temp.add(cluster2);
                        } else {
                            temp.add(eachCluster);
                        }
                    }
                    clusters = temp;
                }
            }

            if (clusters.isEmpty()){ return null;}

        }
        catch (Exception e){
            handleException(e);
        }
        finally {
            Log.exit(clusters);
        }

        //return clusters;
        return ok(new Gson().toJson(clusters));
    }

    /**
     * get the lists of users in list inside a list based on the startTime and endTime
     * @param startTime
     * @param endTime
     * @return List inside a List with userNames
     */
    public List<List<String>>loadChatBuddies(String startTime, String endTime) {
        List<String> pair = null;
        List<List<String>> buddies = null;

        try{
            List<List<UserPO>> buddiesPOs = DAOFactory.getInstance().getUserDAO().loadChatBuddiesByTime(startTime,endTime);

            buddies = new ArrayList<List<String>>();
            for (List<UserPO> pairPO : buddiesPOs ){
                pair = new ArrayList<String>();
                for (UserPO userPO : pairPO){
                    String dto = ConverterUtils.convert(userPO).getUserName();
                    pair.add(dto);
                }
                buddies.add(pair);
            }
        }
        catch (Exception e) {
            handleException(e);
        }

        return buddies;
    }

    /**
     * get the details of all the Users in the SSNOC Application
     * @return List of AllUsers in SSNOC
     */
    public List<String> loadAllUsers() {
        List<String> allUsers = null;

        try{
            List<UserPO> userPOs = DAOFactory.getInstance().getUserDAO().loadUsers();
            allUsers = new ArrayList<>();
            for (UserPO po : userPOs) {
                String dto = ConverterUtils.convert(po).getUserName();
                allUsers.add(dto);
            }
        }
        catch (Exception e) {
            handleException(e);
        }
        return allUsers;
    }
}
	