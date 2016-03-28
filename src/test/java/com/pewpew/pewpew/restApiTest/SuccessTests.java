package com.pewpew.pewpew.restApiTest;

import com.pewpew.pewpew.common.RandomString;
import com.pewpew.pewpew.main.GsonMessageBodyHandler;
import com.pewpew.pewpew.main.RestApplication;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.rest.ScoreboardService;
import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import org.bson.types.ObjectId;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class SuccessTests extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(SessionService.class,
                UserService.class, ScoreboardService.class);
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(GsonMessageBodyHandler.class);
    }

    private static NewCookie newCookie;

    @Test
    public void testSignUp() {
        RandomString randomString = new RandomString();
        User userProfile = new User();
        userProfile.setLogin(randomString.nextString());
        userProfile.setPassword(randomString.nextString());
        userProfile.setEmail(randomString.nextString());
        final String json = target("user").request("application/json").post(Entity.json(userProfile), String.class);
        assertNotNull(json);
    }

    @Test
    public void testSignIn() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");

        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(ObjectId.class).toString();

        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        Response userInfo = target("user").path(id).request().cookie(newCookie).get();
        User returnedUser = userInfo.readEntity(User.class);
        assertEquals(user.getLogin(), returnedUser.getLogin());
        assertNotNull(returnedUser.getEmail());
        assertEquals(returnedUser.getId().toString(), id);
    }

    @Test
    public void testEditUser() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(ObjectId.class).toString();
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        user.setLogin("222");
        Response userInfo = target("user").path(id).request().cookie(newCookie).put(Entity.json(user));
        String userID = userInfo.readEntity(ObjectId.class).toString();
        assertEquals(userID, id);

        user.setLogin("111");
        userInfo = target("user").path("id").request().cookie(newCookie).put(Entity.json(user));
        userID = userInfo.readEntity(ObjectId.class).toString();
        assertEquals(userID, id);
    }

    @Test
    public void testCheckAuth() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(ObjectId.class).toString();
        assertNotNull(id);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        final Response authStateJson = target("session").request().cookie(newCookie).get();
        assertEquals(authStateJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testLogout() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(ObjectId.class).toString();
        assertNotNull(id);

        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");
        final Response authStateJson = target("session").request().cookie(newCookie).delete();
        assertEquals(authStateJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testDelete() {
        RandomString randomString = new RandomString();
        User userProfile = new User();
        userProfile.setLogin(randomString.nextString());
        userProfile.setPassword(randomString.nextString());
        userProfile.setEmail(randomString.nextString());
        final Response json = target("user").request("application/json")
                .post(Entity.json(userProfile));
        assertEquals(json.getStatus(), Response.Status.OK.getStatusCode());

        final Response idJson = target("session").request().post(Entity.json(userProfile));
        String id = idJson.readEntity(ObjectId.class).toString();

        final Response deleteJson = target("user").path(id).request().delete();
        assertEquals(deleteJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testScroyboard() {
        final Response scroyBoard = target("scoreboard").request("application/json").get();
        List<User> users = scroyBoard.readEntity(new ListGenericType());
        assertNotNull(users);
        assertFalse(users.size() < 2);
        Integer firstUserRating = users.get(0).getRating();
        Integer secondUserRating = users.get(1).getRating();
        assertNotNull(firstUserRating);
        assertNotNull(secondUserRating);
        assertTrue(firstUserRating >= secondUserRating);
    }

    private static class ListGenericType extends GenericType<List<User>> {}
}