package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.mechanics.GameMechanics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import java.time.LocalDateTime;

public class GameSocketCreator implements WebSocketCreator {
    static final Logger LOGGER = LogManager.getLogger(GameSocketCreator.class);

    private final WebSocketService webSocketService;
    private final GameMechanics gameMechanics;

    public GameSocketCreator(WebSocketService webSocketService,
                              GameMechanics gameMechanics) {
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }
    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest,
                                  ServletUpgradeResponse servletUpgradeResponse) {
//        Cookie[] cookies = servletUpgradeRequest.getHttpServletRequest().getCookies();
//        if (cookies == null) {
//            LOGGER.error("No cookies");
//            return null;
//        }
//        String user = null;
//        for (Cookie cookie : cookies) {
//            if (cookie.getName().equals("token")) {
//                user = accountService.getUserByToken(cookie.getName()).getLogin();
//            }Serialized user and sendingopen websocket
//        }
//        if (user == null) {
//            LOGGER.error("No such user");
//            return null;
//        }
        final String session = servletUpgradeRequest.getHttpServletRequest().getSession().getId() + LocalDateTime.now().toString();
        LOGGER.info("Socket created");
        return new GameWebSocket(session, webSocketService, gameMechanics);
    }
}
