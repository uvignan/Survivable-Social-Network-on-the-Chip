
package edu.cmu.sv.ws.ssnoc.test;

        import org.junit.Assert;
        import org.junit.Rule;
        import org.junit.runner.RunWith;

        import com.eclipsesource.restfuse.Destination;
        import com.eclipsesource.restfuse.HttpJUnitRunner;
        import com.eclipsesource.restfuse.MediaType;
        import com.eclipsesource.restfuse.Method;
        import com.eclipsesource.restfuse.Response;
        import com.eclipsesource.restfuse.annotation.Context;
        import com.eclipsesource.restfuse.annotation.HttpTest;

        import static com.eclipsesource.restfuse.Assert.*;

@RunWith(HttpJUnitRunner.class)
    public class CreateUsers {
    @Rule
    public Destination destination = new Destination(this,
            "http://localhost:1234/ssnoc");

    @Context
    public Response response;


        @HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
                content = "{\"userName\":\"tester1\",\"password\":\"12345\"}")
         public void createUsers1() {
            assertCreated(response);
        }

        @HttpTest(method = Method.POST, path = "/user/signup", type = MediaType.APPLICATION_JSON,
                content = "{\"userName\":\"tester2\",\"password\":\"12345\"}")
        public void createUsers2() {
            assertCreated(response);
        }


        @HttpTest(method = Method.POST, path = "/message/tester1", type = MediaType.APPLICATION_JSON,
                content = "{\"content\":\"hello1fortest\"}")
        public void testSendPublicWall_Pos() {
            assertOk(response);
            String Messg = response.getBody();
            Assert.assertEquals("wall message saved",Messg);
        }

        @HttpTest(method = Method.POST, path = "/message/notExist", type = MediaType.APPLICATION_JSON,
                content = "{\"content\":\"hello1fortest\"}")
        public void testSendPublicWall_Neg() {
            assertConflict(response);
        }

        @HttpTest(method = Method.GET, path = "/messages/wall")
        public void testGetPublicWall() {
            assertOk(response);
        }

        @HttpTest(method = Method.POST, path = "/message/tester1/tester2", type = MediaType.APPLICATION_JSON,
                    content = "{\"content\":\"nice2meetUFrom1\"}")
        public void testSendPrivateChat_Pos() {
            assertCreated(response);
        }

        @HttpTest(method = Method.POST, path = "/message/tester1/notExist", type = MediaType.APPLICATION_JSON,
                content = "{\"content\":\"nice2meetUFrom1\"}")
        public void testSendPrivateChat_Neg() {
            assertNotFound(response);
            String Messg = response.getBody();
            Assert.assertEquals("Unauthorized User: notExist",Messg);
        }

        @HttpTest(method = Method.GET, path = "/messages/tester1/tester2")
        public void testGetPrivateChats_Pos() {
            assertOk(response);
        }

        @HttpTest(method = Method.GET, path = "/messages/tester1/notExist")
        public void testGetPrivateChats_Neg() {
            assertNotFound(response);
            String Messg = response.getBody();
            Assert.assertEquals("Unauthorized User: notExist",Messg);
        }

        @HttpTest(method = Method.GET, path = "/users/tester1/chatbuddies")
        public void testGetBuddies_Pos() {
            assertOk(response);
        }

        @HttpTest(method = Method.GET, path = "/users/notExist/chatbuddies")
        public void testGetBuddies_Neg() {
            assertNotFound(response);
            String Messg = response.getBody();
            Assert.assertEquals("Unauthorized User: notExist",Messg);
        }




}
