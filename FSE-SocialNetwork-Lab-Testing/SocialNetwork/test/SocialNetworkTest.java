import static org.junit.Assert.*;

import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class SocialNetworkTest {


    SocialNetwork sn;
    Account me, her, another, yetanother;
    Collection<String> members;
	@Before
	public void setUp() throws Exception {
        sn = new SocialNetwork();
	}

	@After
	public void tearDown() throws Exception {
	
	}

	@Test 
	public void canJoinSocialNetwork() {
		me = sn.join("Hakan");
		assertEquals("Hakan", me.getUserName());
	}

    @Test
    public void canEmptyJoinSocialNetwork(){
        yetanother = sn.join("");
        assertNull(yetanother);
    }
    @Test
    public void canNullJoinSocialNetwork(){
    	yetanother = sn.join(null);
    	assertNull(yetanother);
    }
    

	@Test 
	public void canCheckSingleMemberInListOfSocialNetwork() {
		sn.join("Hakan");
		members = sn.listMembers();
		assertEquals(1, members.size());
		assertTrue(members.contains("Hakan"));
	}
	
	@Test 
	public void twoPeopleCanJoinSocialNetwork() {
		sn.join("Hakan");
		sn.join("Cecile");
		members = sn.listMembers();
		assertEquals(2, members.size());
		assertTrue(members.contains("Hakan"));
		assertTrue(members.contains("Cecile"));
	}

    @Test
    public void canListAllMemberJoinedSocialNetwork(){
        sn.join("Hakan");
        sn.join("Cecile");
        sn.join("Serra");
        members = sn.listMembers();
        assertEquals(3,members.size());
    }


	
	@Test 
	public void aMemberCanSendAFriendRequestToAnotherMember() {
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendRequestTo("Cecile", me);
		assertTrue(her.whoWantsToBeFriends().contains("Hakan"));
	}
	
	@Test 
	public void aMemberCanAcceptAFriendRequestFromAnotherMember() {
		me = sn.join("Hakan");
		her = sn.join("Cecile");
		sn.sendFriendRequestTo("Cecile", me);
		sn.acceptFriendshipFrom("Hakan", her);
		assertTrue(me.hasFriend("Cecile"));
		assertTrue(her.hasFriend("Hakan"));
	}

    @Test
    public void aMemberCanAcceptsAllFriendRequestsFromOtherMembers(){
        me = sn.join("Hakan");
        her = sn.join("Cecile");
        another = sn.join("Serra");
        sn.sendFriendRequestTo("Hakan",her);
        sn.sendFriendRequestTo("Hakan",another);
        sn.acceptAllFriendRequestsTo(me);
        assertTrue(me.hasFriend("Cecile"));
        assertTrue(her.hasFriend("Hakan"));
        assertTrue(me.hasFriend("Serra"));
        assertTrue(another.hasFriend("Hakan"));
    }

    @Test
    public void aMemberCanRejectAFriendRequestFromAnotherMember(){
        me = sn.join("Hakan");
        her = sn.join("Cecile");
        sn.sendFriendRequestTo("Hakan",her);
        sn.rejectFriendRequestFrom(her.getUserName(),me);
        assertFalse(me.whoWantsToBeFriends().contains("Cecile"));
    }

    @Test
    public void aMemberCanRejectAllFriendRequests(){
        me=sn.join("Hakan");
        her=sn.join("Cecile");
        another = sn.join("Serra");
        sn.sendFriendRequestTo("Hakan",her);
        sn.sendFriendRequestTo("Hakan",another);
        sn.rejectAllFriendRequestsTo(me);
        assertFalse(me.whoWantsToBeFriends().contains("Cecile"));
        assertFalse(me.whoWantsToBeFriends().contains("Serra"));
    }

    @Test
    public void aMemberCanAutomaticallyAcceptAFriendRequest(){
        me=sn.join("Hakan");
        her=sn.join("Cecile");
        sn.autoAcceptFriendRequestsTo(me);
        another = sn.join("Serra");
        sn.sendFriendRequestTo("Hakan",her);
        sn.sendFriendRequestTo("Hakan",another);
        assertEquals(2,me.getFriends().size());
        assertTrue(her.hasFriend("Hakan"));
        assertTrue(another.hasFriend("Hakan"));
        assertTrue(me.hasFriend("Cecile"));
        assertTrue(me.hasFriend("Serra"));
    }

    @Test
    public void aMemberCanUnfriendAnotherMember(){
        me=sn.join("Hakan");
        sn.autoAcceptFriendRequestsTo(me);
        her=sn.join("Cecile");
        another=sn.join("Serra");
        sn.sendFriendRequestTo("Hakan",her);
        sn.sendFriendRequestTo("Hakan",another);
        sn.sendFriendRequestTo(null, me);
        sn.sendUnfriendRequestTo(her.getUserName(),me);
        assertFalse(me.hasFriend("Cecile"));
        assertFalse(her.hasFriend("Hakan"));
     }

    @Test
    public void canLeaveSocialNetwork(){
        me=sn.join("Hakan");
        assertEquals("Hakan",me.getUserName());
        assertTrue(sn.listMembers().contains("Hakan"));
        sn.leave(me);
        assertFalse(sn.listMembers().contains("Hakan"));
    }

}
