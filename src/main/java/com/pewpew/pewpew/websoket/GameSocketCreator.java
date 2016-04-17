package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.Cookie;
import java.time.LocalDateTime;
import java.util.Set;

public class GameSocketCreator implements WebSocketCreator {
    static final Logger LOGGER = LogManager.getLogger(GameSocketCreator.class);
    private Set<GameWebSocket> users;

    private AccountService accountService;
    private WebSocketService webSocketService;
    private GameMechanics gameMechanics;

    public GameSocketCreator(AccountService accountService, WebSocketService webSocketService,
                              GameMechanics gameMechanics) {
        this.accountService = accountService;
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
//            }
//        }
//        if (user == null) {
//            LOGGER.error("No such user");
//            return null;
//        }
        String session = servletUpgradeRequest.getHttpServletRequest().getSession().getId() + LocalDateTime.now().toString();
        GameWebSocket gameWebSocket = new GameWebSocket(session, webSocketService, gameMechanics);
        LOGGER.info("Socket created");
        return gameWebSocket;
    }
}
