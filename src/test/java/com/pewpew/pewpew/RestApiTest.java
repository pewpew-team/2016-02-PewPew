package com.pewpew.pewpew;

import com.pewpew.pewpew.additional.RandomString;
import com.pewpew.pewpew.main.RestApplication;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.model.UserAuth;
import com.sun.tools.javac.util.List;
import org.glassfish.jersey.client.ClientResponse;
import org.glassfish.jersey.server.model.Resource;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class RestApiTest extends JerseyTest {
        @Override
        protected Application configure() {
            return new RestApplication();
        }

    private static NewCookie newCookie;

        @Test
        public void testCreateUser() {
            User userProfile = new User();
            RandomString randomString = new RandomString();
            userProfile.setLogin(randomString.nextString());
            userProfile.setPassword(randomString.nextString());
            userProfile.setEmail(randomString.nextString());
            final String json = target("user").request("application/json").post(Entity.json(userProfile), String.class);
            assertNotNull(json);
        }

    @Test
    public void testSignIn() {
        UserAuth user = new UserAuth();
        user.setLogin("123");
        user.setPassword("123");
        final Response json = target("session").request().post(Entity.json(user));
        String id = json.readEntity(String.class);
        final Map<String, NewCookie> cookies = json.getCookies();
        this.newCookie = cookies.get("token");
        final Response userInfo = target("user").path("id").request().cookie(this.newCookie).get();
        User l = json.readEntity(User.class);
        String a = "0";
    }

//    @Test
//    public void testGetUserInfo() {
//        final String userInfo = target("user").path("123sd").request().cookie(this.newCookie).get(String.class);
//        String a = "0";
//    }
}
