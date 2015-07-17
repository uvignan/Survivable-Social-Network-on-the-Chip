package edu.cmu.sv.ws.ssnoc.data.dao;

/**
 * Created by vignan on 10/8/14.
 */

import edu.cmu.sv.ws.ssnoc.data.po.ExchangeInfoPO;
import edu.cmu.sv.ws.ssnoc.data.po.UserPO;
import edu.cmu.sv.ws.ssnoc.dto.ExchangeInfo;

import java.util.List;

/**
 * Interface specifying the contract that all implementations will implement to
 * provide persistence of User Chat information in the system.
 *
 */
public interface IMessageDAO {

    /**
     * This method will save the information of the user into the database.
     *
     * @param userPO,einfoPO
     *            - User information to be saved.
     */
    void saveWallMessage(UserPO userPO, ExchangeInfoPO einfoPO);

    void saveAnnouncement(UserPO userPO, ExchangeInfoPO einfoPO);

    void saveChatMessage(UserPO po1, UserPO po2, ExchangeInfoPO einfopo);

    List<ExchangeInfoPO> loadWallMessages();

    List<ExchangeInfoPO> loadAllAnnouncements();

    List<ExchangeInfoPO> loadChatMessages(UserPO po1, UserPO po2);

    List<ExchangeInfoPO> loadChatBuddies(UserPO po);

    List<ExchangeInfoPO> loadVisibleWallMessages();

    List<ExchangeInfoPO> loadVisibleAnnouncements();

    List<ExchangeInfoPO> loadVisibleChatMessages(UserPO po1, UserPO po2);

}
