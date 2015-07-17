package edu.cmu.sv.ws.ssnoc.data.dao;


public interface ISearchDAO {
    String QueryName(String[] query);
    String QueryStatus(String[] query);
    String QueryAnnouncements(String[] query);
    String QueryPublicMessage(String[] query);
    String QueryPrivateMessages(String[] query);
}
