package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;

import edu.cmu.sv.ws.ssnoc.data.util.DBUtils;


import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;


/**
 * Created by Vignan on 10/14/2014.
 */

@Path("/performance")
public class MeasurePerformanceService extends BaseService {

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/setup")
    public Response startPerformanceMeasure(){
        try {
            DBUtils.setPerformaceRunning();

        } catch (SQLException e) {
            Log.error("Oops :( We ran into an error when trying to intialize "
                    + "Perf Test database. Please check the trace for more details.", e);
        }

        return ok("Database Created");

    }

    @POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/teardown")
    public void stopPerformanceMeasure(){
       try {
            DBUtils.stopPerformanceRunning();
        }
        catch (SQLException e){
            Log.error("Oops :( We ran into an error when trying to shutdown and delete "
                    + "Perf Test database. Please check the trace for more details.", e);
        }
    }


}
