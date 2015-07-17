package edu.cmu.sv.ws.ssnoc.data.dao;

import edu.cmu.sv.ws.ssnoc.data.po.MemoryPO;

import java.util.List;

/**
 * Created by Vignan on 10/14/2014.
 */
public interface IMemoryDAO {

    void insertMemoryStats(MemoryPO memDetails);

    void deleteMemoryCrumbData();

    List<MemoryPO> getMemoryStats(String toDate, String fromDate);
}
