package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.*;
import com.pewpew.pewpew.model.User;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import java.util.Map;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("unused")
public class ErrorTest extends JerseyTest {

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
    public void testErrorSignUp() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        user.setEmail("11@mail.ru");
        final Response json = target("user").request("application/json").post(Entity.json(user));
        assertEquals(Response.Status.CONFLICT.getStatusCode(), json.getStatus());
    }

    @Test
    public void testErrorSighIn() {
        final User user = new User();
        user.setLogin("i'm not existed user");
        user.setPassword("111");

        final Response errorJson = target("session").request().post(Entity.json(user));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), errorJson.getStatus());
    }

    @Test
    public void testValidateSignIn() {
        final User user = new User();
        user.setLogin("i'm not existed user");
        final Response errorJson = target("session").request().post(Entity.json(user));
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), errorJson.getStatus());
    }

    @Test
    public void testDoubleSignIn() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");

        final Response firstSignIn = target("session").request().post(Entity.json(user));
        final Map<String, NewCookie> cookies = firstSignIn.getCookies();
        newCookie = cookies.get("token");
        final Response secondSignIn = target("session").request().cookie(newCookie).post(Entity.json(user));
        assertEquals(Response.Status.OK.getStatusCode(), secondSignIn.getStatus());
    }

    @Test
    public void testWrongTokenSighIn() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final NewCookie cookie = new NewCookie("token", "123");
        final Response errorJson = target("session").request().cookie(cookie).post(Entity.json(user));
        assertEquals(Response.Status.OK.getStatusCode(), errorJson.getStatus());
    }


    @Test
    public void testErrorCheckAuth() {
        final NewCookie cookie = new NewCookie("token", "123");
        final Response errorJson = target("session").request().cookie(cookie).get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), errorJson.getStatus());
    }

    @Test
    public void testNoTokenCheckAuth() {
        final Response errorJson = target("session").request().get();
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), errorJson.getStatus());
    }

    @Test
    public void testErrorChangeUserInfo() {
        final User user = new User();
        user.setLogin("111");
        user.setPassword("111");
        final Response userInfo = target("user").path("/123").request().cookie(newCookie).put(Entity.json(user));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), userInfo.getStatus());
    }

    @Test
    public void testErrorGetUserById() {
        final Response userInfo = target("user").path("/123").request().cookie(newCookie).get();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), userInfo.getStatus());
    }

    @Test
    public void testWrongIdUserById() {
        final String id = "56f94e8a67bc2e7632974677";
        final Response userInfo = target("user").path(id).request().cookie(newCookie).get();
        assertEquals(Response.Status.CONFLICT.getStatusCode(), userInfo.getStatus());
    }

    @Test
    public void testErrorLogout() {
        final NewCookie cookie = new NewCookie("token", "123");
        final Response json = target("session").request().cookie(cookie).delete();
        assertEquals(json.getStatus(), Response.Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    public void testNoTokenLogout() {
        final Response json = target("session").request().delete();
        assertEquals(json.getStatus(), Response.Status.OK.getStatusCode());
    }

    @Test
    public void testErrorNotIdDelete() {
        final String id = "1111";
        final Response deleteJson = target("user").path(id).request().delete();
        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), deleteJson.getStatus());
    }

    @Test
    public void testErrorDoubleDelete() {
        final String id = "56f94e8a67bc2e7632974677";
        final Response deleteJson = target("user").path(id).request().delete();
        assertEquals(deleteJson.getStatus(), Response.Status.FORBIDDEN.getStatusCode());
    }
}
