package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.StatusPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.Status;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vignan on 9/29/14.
 */

@Path("/statuscrumbs")
public class UserStatus extends BaseService{
/**
 * This method loads all statuses of a user in the system.
 *
 * @return - user statuses
 */
@GET
@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
@Path("/{userName}")
@XmlElementWrapper(name = "statuscrumbs")
public List<Status> loadUsersStatuses(@PathParam("userName") String userName) {
    Log.enter();
    List<Status> statuses = null;

    try {
        UserPO po = loadExistingUser(userName);
        List<StatusPO> statusPOs = DAOFactory.getInstance().getUserDAO().loadStatuses(po.getUserId());

        statuses = new ArrayList<Status>();
        for (StatusPO spo : statusPOs) {
            Status sdto = ConverterUtils.convert(spo);
            statuses.add(sdto);
        }
    } catch (Exception e) {
        handleException(e);
    } finally {
        Log.exit(statuses);
    }

    return statuses;
}


}
