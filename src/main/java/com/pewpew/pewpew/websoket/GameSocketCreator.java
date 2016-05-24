package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.jetbrains.annotations.Nullable;

import javax.servlet.http.Cookie;

public class GameSocketCreator implements WebSocketCreator {
    static final Logger LOGGER = LogManager.getLogger(GameSocketCreator.class);

    private final AccountService accountService;
    private final MessageSystem messageSystem;
    private final Address gameMechanicsAddress;

    public GameSocketCreator(AccountService accountService, MessageSystem messageSystem,
                             Address gameMechanicsAddress) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.gameMechanicsAddress = gameMechanicsAddress;
    }
    @Nullable
    @Override
    public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest,
                                  ServletUpgradeResponse servletUpgradeResponse) {
        final Cookie[] cookies = servletUpgradeRequest.getHttpServletRequest().getCookies();
        if (cookies == null) {
            LOGGER.error("No cookies");
            return null;
        }
        String user = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("token")) {
                user = accountService.getUserByToken(cookie.getValue()).getLogin();
            }
        }
        if (user == null) {
            LOGGER.error("No such user");
            return null;
        }
        LOGGER.info("Socket created");
        final GameWebSocket gameWebSocket = new GameWebSocket(user, messageSystem, gameMechanicsAddress);
        gameWebSocket.start();
        return gameWebSocket;
    }
}
