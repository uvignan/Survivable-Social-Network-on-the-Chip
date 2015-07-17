
import java.util.Collection;
import java.util.HashSet;

public class Account  {
	private String userName;
	private Collection<String> pendingResponses = new HashSet<String>();
	private Collection<String> friends = new HashSet<String>();
    private Collection<String> sentFriendRequests = new HashSet<String>();
    private boolean autoAccept;
	
	public Account(String userName) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public Collection<String> whoWantsToBeFriends() {
        return pendingResponses;
	}

	/* a friend request from another account */
	public void befriend(Account fromAccount) {
		if (fromAccount==null || fromAccount.getUserName()=="") return;
        if (!friends.contains(fromAccount.getUserName())) {
                pendingResponses.add(fromAccount.getUserName());
                fromAccount.sentFriendRequests.add(this.getUserName());
            if(autoAccept){
                fromAccount.accepted(this);
            }
        }
	}

	/* an acceptance notification from an account that 
	 * a friend request sent from this account 
	 * has been accepted
	 */
	public void accepted(Account toAccount) {
		if(toAccount.pendingResponses.contains(this.getUserName()) && sentFriendRequests.contains(toAccount.getUserName())){
		friends.add(toAccount.getUserName());
		toAccount.friends.add(this.getUserName());
		toAccount.pendingResponses.remove(this.getUserName());
		sentFriendRequests.remove(toAccount.getUserName());
		}
	}

	public boolean hasFriend(String userName) {
		return friends.contains(userName);
	}

    public Collection<String> whoDidIAskToBefriend(){
        return sentFriendRequests;
    }

    public void rejected(Account me) {
    	if(me.pendingResponses.contains(this.userName) && sentFriendRequests.contains(me.getUserName())){
        me.pendingResponses.remove(this.getUserName());
        sentFriendRequests.remove(me.getUserName());
    	}
    }

    public void unfriend(Account me) {
        me.friends.remove(this.userName);
        friends.remove(me.getUserName());
    }

    public Collection<String> getFriends(){
        return friends;
    }


    public void autoAccept(){
        autoAccept = true;
    }

}
