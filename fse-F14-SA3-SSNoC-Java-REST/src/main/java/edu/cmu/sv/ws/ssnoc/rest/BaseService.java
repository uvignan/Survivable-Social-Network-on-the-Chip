package edu.cmu.sv.ws.ssnoc.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import edu.cmu.sv.ws.ssnoc.common.exceptions.CheckedException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.ServiceException;
import edu.cmu.sv.ws.ssnoc.common.exceptions.UnknownUserException;
import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;

    /**
     *creating a class BaseService which is extended by other service classes
     */
    public class BaseService {
        /**
         *creating a Exception method
         */
        protected void handleException(Exception e) {
            Log.error(e);
            //used to check the web application exception
            if (e instanceof CheckedException) {
                throw (CheckedException) e;
            } else {
                throw new ServiceException(e);
            }
	}

    /**
     *generating an response class method with no parameters and OK response
     */
	protected Response ok() {
		return Response.status(Status.OK).build();
	}

    /**
     *generating an response class method with object parameter OK response
     */
	protected Response ok(Object obj) {
        return Response.status(Status.OK).entity(obj).build();
	}
    /**
     *generating an response class method with object parameter with created response
     */
	protected Response created(Object obj) {
		return Response.status(Status.CREATED).entity(obj).build();
	}

        /**
         * getting the details of the existing user
         */
	protected UserPO loadExistingUser(String userName) {
		UserPO po = DAOFactory.getInstance().getUserDAO().findByName(userName);
		if (po == null) {
			throw new UnknownUserException(userName);
		}
		return po;
	}
}
