package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.common.RandomString;
import com.pewpew.pewpew.main.*;
import com.pewpew.pewpew.model.User;
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

@SuppressWarnings("unused")
public class SuccessTest extends JerseyTest {

    @Override
    protected Application configure() {
        final Context context = new Context();
        context.put(AccountService.class, new AccountServiceImpl());

        final ResourceConfig config = new ResourceConfig(SessionService.class,
                UserService.class, ScoreboardService.class, GsonMessageBodyHandler.class);
        config.register(new MyAbstractBinder(context));
        return config;
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(GsonMessageBodyHandler.class);
    }

    private static NewCookie newCookie;

    @Test
    public void testSignUp() {
        final RandomString randomString = new RandomString();
        final User userProfile = new User();
        userProfile.setLogin(randomString.nextString());
        userProfile.setPassword(randomString.nextString());
        userProfile.setEmail(randomString.nextString());
        final String json = target("user").request("application/json").post(Entity.json(userProfile), String.class);
        assertNotNull(json);
        assert(json.contains("id"));
    }

    @Test
    public void testSignIn() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");

        final Response json = target("session").request().post(Entity.json(user));
        final String id = json.readEntity(ObjectId.class).toString();

        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        final Response userInfo = target("user").path(id).request().cookie(newCookie).get();
        final User returnedUser = userInfo.readEntity(User.class);
        assertEquals(user.getLogin(), returnedUser.getLogin());
        assertNotNull(returnedUser.getEmail());
        assertEquals(returnedUser.getId().toString(), id);
    }

    @Test
    public void testEditUser() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        final String id = json.readEntity(ObjectId.class).toString();
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
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        final String id = json.readEntity(ObjectId.class).toString();
        assertNotNull(id);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        final Response authStateJson = target("session").request().cookie(newCookie).get();
        assertEquals(authStateJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testLogout() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        final String id = json.readEntity(ObjectId.class).toString();
        assertNotNull(id);

        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");
        final Response authStateJson = target("session").request().cookie(newCookie).delete();
        assertEquals(authStateJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testDelete() {
        final RandomString randomString = new RandomString();
        final User userProfile = new User();
        userProfile.setLogin(randomString.nextString());
        userProfile.setPassword(randomString.nextString());
        userProfile.setEmail(randomString.nextString());
        final Response json = target("user").request("application/json")
                .post(Entity.json(userProfile));
        assertEquals(json.getStatus(), Response.Status.OK.getStatusCode());

        final Response idJson = target("session").request().post(Entity.json(userProfile));
        final String id = idJson.readEntity(ObjectId.class).toString();

        final Response deleteJson = target("user").path(id).request().delete();
        assertEquals(deleteJson.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testScroyboard() {
        final Response scroyBoard = target("scoreboard").request("application/json").get();
        final List<User> users = scroyBoard.readEntity(new ListGenericType());
        assertNotNull(users);
        assertFalse(users.size() < 2);
        final Integer firstUserRating = users.get(0).getRating();
        final Integer secondUserRating = users.get(1).getRating();
        assertNotNull(firstUserRating);
        assertNotNull(secondUserRating);
        assertTrue(firstUserRating >= secondUserRating);
    }

    @SuppressWarnings("unused")
    private static class ListGenericType extends GenericType<List<User>> {}

}