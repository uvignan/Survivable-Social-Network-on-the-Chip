import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


public class AccountTest {
	
	Account me, her, another;
	
	@Before
	public void setUp() throws Exception {
		me = new Account("Hakan");
		her = new Account("Serra");
		another = new Account("Cecile");
	}

	@Test
	public void canBefriendAnother() {
		me.befriend(her);
		assertTrue(me.whoWantsToBeFriends().contains(her.getUserName()));
	}
	
	@Test
	public void noFriendRequestsForAccounts() {
		assertEquals(0, me.whoWantsToBeFriends().size());
	}
	
	@Test
	public void testMultipleFriendRequests() {
		me.befriend(her);
		me.befriend(another);
		assertEquals(2, me.whoWantsToBeFriends().size());
		assertTrue(me.whoWantsToBeFriends().contains(another.getUserName()));
		assertTrue(me.whoWantsToBeFriends().contains(her.getUserName()));
	}
	
	@Test
	public void doubleRedundantFriendRequestsListsAsOnlyOne() {
		me.befriend(her);
		me.befriend(her);
		assertEquals(1, me.whoWantsToBeFriends().size());
	}
	
	@Test
	public void afterFriendRequestAcceptedWhoWantsToBeFriendsUpdated() {
		me.befriend(her);
		her.accepted(me);
		assertFalse(me.whoWantsToBeFriends().contains(her.getUserName()));
	}
	
	@Test
	public void everybodyAreFriends() {
		me.befriend(her);
		me.befriend(another);
		her.befriend(another);
		her.accepted(me);
		another.accepted(her);
		another.accepted(me);
		assertTrue(me.hasFriend(her.getUserName()));
		assertTrue(me.hasFriend(another.getUserName()));
		assertTrue(her.hasFriend(me.getUserName()));
		assertTrue(her.hasFriend(another.getUserName()));
		assertTrue(another.hasFriend(her.getUserName()));
        assertTrue(another.hasFriend(me.getUserName()));
	}
	
	@Test
	public void cannotBefriendAnExistingFriend() {
		me.befriend(her);
		her.accepted(me);
		me.befriend(her);
		assertFalse(me.whoWantsToBeFriends().contains(her.getUserName()));
		assertFalse(her.whoWantsToBeFriends().contains(me.getUserName()));
	}

    @Test
    public void canListFriendRequestsSent(){
        her.befriend(me);
        another.befriend(me);
        assertEquals(2,me.whoDidIAskToBefriend().size());
    }

    @Test
    public void sentRequestsUpdatedOnRejection(){
        her.befriend(me);
        her.rejected(me);
        assertFalse(her.whoDidIAskToBefriend().contains(me.getUserName()));
    }

    @Test
    public void twoSentRequestsUpdatedOnRejections(){
        her.befriend(me);
        another.befriend(me);
        her.rejected(me);
        another.rejected(me);
        assertFalse(her.whoDidIAskToBefriend().contains(me.getUserName()));
        assertFalse(another.whoDidIAskToBefriend().contains(me.getUserName()));
    }

    @Test
    public void pendingResponsesUpdatedOnAutoAcceptEnabled(){
        me.autoAccept();
        her.befriend(me);
        assertFalse(me.whoWantsToBeFriends().contains(her.getUserName()));
    }

    @Test
    public void friendListUpdatedAfterUnFriend(){
        me.autoAccept();
        her.befriend(me);
        me.unfriend(her);
        assertFalse(me.hasFriend(her.getUserName()));
        assertFalse(her.hasFriend(me.getUserName()));
    }

}
