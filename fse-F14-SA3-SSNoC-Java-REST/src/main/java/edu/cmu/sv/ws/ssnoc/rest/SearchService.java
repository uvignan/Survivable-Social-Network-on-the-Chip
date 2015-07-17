package edu.cmu.sv.ws.ssnoc.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.ws.rs.core.Response;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.ISearchDAO;
import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.SearchPO;
import edu.cmu.sv.ws.ssnoc.dto.Search;

@Path("/search")
public class SearchService extends BaseService{

    @POST
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public String processSearchQuery(Search search){
        Log.enter();
        
    	String res = null;

        try{
            SearchPO spo = ConverterUtils.convert(search);
        	ISearchDAO is_dao = DAOFactory.getInstance().getSearchDAO();
        	String[] search_content = spo.getContent();
        	String search_type = spo.getType();
        	switch(search_type){
	        	case "User Search By Name":  
		        	res = is_dao.QueryName(search_content);

		            break;
	        	case "User Search By Status":
		        	res = is_dao.QueryStatus(search_content);

		            break;
	        	case "Public Announcements":
		        	res = is_dao.QueryAnnouncements(search_content);

		            break;
	        	case "Public Messages":
		        	res = is_dao.QueryPublicMessage(search_content);

		            break;
	        	case "Private Messages":
		        	res = is_dao.QueryPrivateMessages(search_content);

		            break;
	        	default: 
	        		Log.warn("illegal type");
        	}
            
        } catch (Exception e){
            handleException(e);
        } finally {
            Log.exit();
        }
        
        return res;

    }


	
}
