package edu.cmu.sv.ws.ssnoc.rest;

import edu.cmu.sv.ws.ssnoc.common.logging.Log;
import edu.cmu.sv.ws.ssnoc.common.utils.ConverterUtils;
import edu.cmu.sv.ws.ssnoc.data.dao.DAOFactory;
import edu.cmu.sv.ws.ssnoc.data.dao.IMemoryDAO;
import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;
import edu.cmu.sv.ws.ssnoc.dto.Memory;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import java.nio.file.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.util.*;

import javax.ws.rs.*;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElementWrapper;

/**
 * Created by Vignan on 10/14/2014.
 */

@Path("/memory")
public class MeasureMemoryService extends BaseService{
    static Timer timeMonitor = new Timer();
    @POST
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @Path("/start")
    public Response startMemoryMonitor(){
            timeMonitor = null;
            timeMonitor = new Timer();
         TimerTask scanTask = new TimerTask() {
             IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
             MemoryPO memDetails = new MemoryPO();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

             @Override
             public void run() {
                 /*Runtime rt=Runtime.getRuntime();
                 long freeVMemory = rt.freeMemory()/1024;
                 long totalVMemory = rt.totalMemory()/1024;
                 long usedVMemory = totalVMemory-freeVMemory;*/

                 OperatingSystemMXBean bean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

                 long maxMemory = bean.getTotalPhysicalMemorySize()/1024;
                 long freeVMemory = bean.getFreePhysicalMemorySize()/1024;

                 long usedVMemory = maxMemory-freeVMemory;

                 long freeSpace = 0;
                 long usedSpace = 0;

                 for(java.nio.file.Path root : FileSystems.getDefault().getRootDirectories())
                 {
                     try{
                    FileStore store = Files.getFileStore(root);
                     freeSpace = store.getUnallocatedSpace()/1024;
                     usedSpace = ((store.getTotalSpace()/1024)-(store.getUnallocatedSpace()/1024));
                     }catch (FileSystemException e){
                         Log.trace(e.toString());
                     }catch (IOException e){
                         Log.trace(e.toString());
                     }

                 }
                    memDetails.setUsedVolatile(usedVMemory);
                    memDetails.setRemainingVolatile(freeVMemory);
                    memDetails.setUsedPersistent(usedSpace);
                    memDetails.setRemainingPersistent(freeSpace);
                    Calendar calendar = Calendar.getInstance();
                    memDetails.setCreatedAt(df.format(calendar.getTime()));
                    mdao.insertMemoryStats(memDetails);

             }

         };
        timeMonitor.schedule(scanTask,0,60000);

        return ok();
    }

    @POST
    @Path("/stop")
    public void stopMemoryMonitor(){
            timeMonitor.cancel();
        Log.trace("Memory Monitor Stopped");
    }

    @DELETE
    public void deleteMemoryCrumb(){
        IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
        mdao.deleteMemoryCrumbData();
        Log.trace("Memory Crumb cleared");
    }

    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @XmlElementWrapper(name = "memorystats")
    public List<Memory> loadMemoryStats(){
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String toDate = df.format(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        String fromDate = df.format(calendar.getTime());
        Log.trace(toDate,fromDate);
        List<Memory> memorystats = null;
        try{
            IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
            List<MemoryPO> memPOs = mdao.getMemoryStats(toDate,fromDate);

            memorystats = new ArrayList<Memory>();
            for(MemoryPO memPO : memPOs){
                Memory memdto = ConverterUtils.convert(memPO);
                memorystats.add(memdto);
            }
        }catch (Exception e){
            handleException(e);
        }finally {
            Log.exit(memorystats);
        }
        return memorystats;
    }


    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @XmlElementWrapper(name = "memorystats")
    @Path("/interval/{timeWindowsInHours}")
    public List<Memory> loadMemoryStatusInInterval(@PathParam("timeWindowsInHours") int timeWindowsInHours){
        Calendar calendar = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String toDate = df.format(calendar.getTime());
        calendar.add(Calendar.HOUR_OF_DAY, -timeWindowsInHours);
        String fromDate = df.format(calendar.getTime());
        Log.trace(toDate,fromDate);
        List<Memory> memorystats = null;
        try{
            IMemoryDAO mdao = DAOFactory.getInstance().getMemoryDAO();
            List<MemoryPO> memPOs = mdao.getMemoryStats(toDate,fromDate );

            memorystats = new ArrayList<Memory>();
            for(MemoryPO memPO : memPOs){
                Memory memdto = ConverterUtils.convert(memPO);
                memorystats.add(memdto);
            }
        }catch (Exception e){
            handleException(e);
        }finally {
            Log.exit(memorystats);
        }
        return memorystats;
    }

}
