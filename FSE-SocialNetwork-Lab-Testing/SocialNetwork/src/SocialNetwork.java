import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SocialNetwork {
	
	private Collection<Account> accounts = new HashSet<Account>();

	public Account join(String userName) {
        if ( userName == null || userName == ""){
         return null;
        }
        if(findAccountForUserName(userName)!=null) return null;
            Account newAccount = new Account(userName);
            accounts.add(newAccount);
            return newAccount;

	}

    public void leave(Account me) {
    	for(Account member : accounts){
    		
    		member.unfriend(me);
    		member.whoWantsToBeFriends().remove(me.getUserName());
    		member.whoDidIAskToBefriend().remove(me.getUserName());
    	}
    	accounts.remove(me);
    	me.whoWantsToBeFriends().clear();
    	me.whoDidIAskToBefriend().clear();
    }

	private Account findAccountForUserName(String userName) {
		for (Account each : accounts) {
			if (each.getUserName().equals(userName))
					return each;
		}
		return null;
	}
	
	public Collection<String> listMembers() {
		Collection<String> members = new HashSet<String>();
		for (Account each : accounts) {
			members.add(each.getUserName());
		}
		return members;
	}	
	public void sendFriendRequestTo(String userName, Account me) {
        if(findAccountForUserName(userName)==null)return;
		findAccountForUserName(userName).befriend(me);
	}

      public void autoAcceptFriendRequestsTo(Account me){
          me.autoAccept();
    }
        
	public void acceptFriendshipFrom(String userName, Account me) {
        if(findAccountForUserName(userName)==null)return;
        findAccountForUserName(userName).accepted(me);
	}


    public void acceptAllFriendRequestsTo(Account me) {
        List<String> myList = new CopyOnWriteArrayList<String>();
        for (String each : me.whoWantsToBeFriends()){
            myList.add(each);
        }
        for(String each : myList ){
            findAccountForUserName(each).accepted(me);
        }
    }

    public void rejectFriendRequestFrom(String herUserName, Account me) {
    	if(findAccountForUserName(herUserName)==null)return;
        findAccountForUserName(herUserName).rejected(me);
    }

    public void rejectAllFriendRequestsTo(Account me) {
        List<String> myList = new CopyOnWriteArrayList<String>();
        for (String each : me.whoWantsToBeFriends()){
            myList.add(each);
        }
        for(String each : myList ){
            findAccountForUserName(each).rejected(me);
        }
    }

    public void sendUnfriendRequestTo(String userName, Account me) {

        if(findAccountForUserName(userName)==null)return;
        findAccountForUserName(userName).unfriend(me);
    }

}
