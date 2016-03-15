package com.pewpew.pewpew.main;

import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@ApplicationPath("api")
public class RestApplication extends Application {
    private  static  final AccountService ACCOUNT_SERVICE = new AccountService();
    @Override
    public Set<Object> getSingletons() {
        final HashSet<Object> objects = new HashSet<>();
        objects.add(new SessionService(ACCOUNT_SERVICE));
        objects.add(new UserService(ACCOUNT_SERVICE));
        objects.add(EntityFilteringFeature.class);
        return objects;
    }

    @Override
    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        properties.put(EntityFilteringFeature.ENTITY_FILTERING_SCOPE, new Annotation[]{
                //
        });
        return super.getProperties();
    }
}

