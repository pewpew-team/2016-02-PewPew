package com.pewpew.pewpew;

import com.pewpew.pewpew.common.RandomString;
import com.pewpew.pewpew.main.GsonMessageBodyHandler;
import com.pewpew.pewpew.main.RestApplication;
import com.pewpew.pewpew.model.User;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RestApiTest extends JerseyTest {




    @Override
    protected Application configure() {
//        ResourceConfig resourceConfig = new ResourceConfig();
//        resourceConfig.packages("com.pewpew.pewpew.main");
//        resourceConfig.register(GsonMessageBodyHandler.class);
        return new RestApplication();
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void configureClient(ClientConfig clientConfig) {
        clientConfig.register(GsonMessageBodyHandler.class);
    }

    private static NewCookie newCookie;

    @Test
    public void testCreateUser() {
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
        String id = json.readEntity(String.class);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");
        Response userInfo = target("user").path("id").request().cookie(newCookie).get();
        User returnedUser = userInfo.readEntity(User.class);
        assertEquals(user.getLogin(), returnedUser.getLogin());
    }

    @Test
    public void testEditUser() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(String.class);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        user.setLogin("222");
        Response userInfo = target("user").path("id").request().cookie(newCookie).put(Entity.json(user));
        String userID = userInfo.readEntity(String.class);
        assertEquals(userID, "56e82db30ae21fc88e43d020");

        user.setLogin("111");
        userInfo = target("user").path("id").request().cookie(newCookie).put(Entity.json(user));
        userID = userInfo.readEntity(String.class);
        assertEquals(userID, "56e82db30ae21fc88e43d020");
    }

    @Test
    public void testCheckAuth() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(String.class);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        final Response authStateJson = target("session").request().cookie(newCookie).get();
        assertEquals(authStateJson.getStatus(), 200);
    }

    @Test
    public void testLogout() {
        User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(String.class);
        final Map<String, NewCookie> cookies = json.getCookies();
        newCookie = cookies.get("token");

        final Response authStateJson = target("session").request().cookie(newCookie).delete();
        assertEquals(authStateJson.getStatus(), 200);
    }

    @Test
    public void testDelete() {
        RandomString randomString = new RandomString();
        User userProfile = new User();
        userProfile.setLogin(randomString.nextString());
        userProfile.setPassword(randomString.nextString());
        userProfile.setEmail(randomString.nextString());
        final String json = target("user").request("application/json").post(Entity.json(userProfile), String.class);

        final Response idJson = target("session").request().post(Entity.json(userProfile));
        String id = idJson.readEntity(String.class);

        final Response deleteJson = target("user").path(id).request().delete();

    }

}