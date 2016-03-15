package com.pewpew.pewpew.main;

import com.pewpew.pewpew.rest.ScoreboardService;
import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import org.glassfish.jersey.message.filtering.EntityFilteringFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.lang.annotation.Annotation;

@ApplicationPath("api")
public class RestApplication extends ResourceConfig {

    private static final AccountService ACCOUNT_SERVICE = new AccountService();
    public RestApplication() {
        property(EntityFilteringFeature.ENTITY_FILTERING_SCOPE, new Annotation[]{
                //
        });
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        register(EntityFilteringFeature.class);

        final SessionService sessionService = new SessionService();
        final UserService userService = new UserService();
        final ScoreboardService scoreboardService = new ScoreboardService();

        register(sessionService);
        register(userService);
        register(scoreboardService);
    }
}

//@ApplicationPath("api")
//public class RestApplication extends Application {
//    private  static  final AccountService ACCOUNT_SERVICE = new AccountService();
//    @Override
//    public Set<Object> getSingletons() {
//        final HashSet<Object> objects = new HashSet<>();
//        objects.add(new SessionService(ACCOUNT_SERVICE));
//        objects.add(new UserService(ACCOUNT_SERVICE));
//        objects.add(EntityFilteringFeature.class);
//        return objects;
//    }
//
//    @Override
//    public Map<String, Object> getProperties() {
//        final Map<String, Object> properties = new HashMap<>();
//        properties.put(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
//        properties.put(EntityFilteringFeature.ENTITY_FILTERING_SCOPE, new Annotation[]{
//                //
//        });
//        return super.getProperties();
//    }
//}

