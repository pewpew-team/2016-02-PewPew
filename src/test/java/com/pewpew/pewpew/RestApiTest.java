package com.pewpew.pewpew;

import com.pewpew.pewpew.common.RandomString;
import com.pewpew.pewpew.main.RestApplication;
import com.pewpew.pewpew.model.User;
import org.glassfish.jersey.test.JerseyTest;
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
        return new RestApplication();
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
        Response userInfo = target("user").path("id").request().cookie(this.newCookie).get();
        Response userInfo2 = target("user").path("id").request().cookie(newCookie).get();
        User returnedUser = userInfo2.readEntity(User.class);
        assertEquals(user.getLogin(), returnedUser.getLogin());
        assertEquals(user.getPassword(), returnedUser.getPassword());
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

//    @Test
//    public void test

//    @Test
//    public void testGetUserInfo() {
//        final String userInfo = target("user").path("123sd").request().cookie(this.newCookie).get(String.class);
//        String a = "0";
//    }
}
