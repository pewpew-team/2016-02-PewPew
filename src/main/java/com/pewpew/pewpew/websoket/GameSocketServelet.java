package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.mechanics.GameMechanics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "GameSocketServelet", urlPatterns = {"/ws"})
public class GameSocketServelet extends WebSocketServlet {
    static final Logger LOGGER = LogManager.getLogger(GameSocketServelet.class);
    private static final int IDLE_TIME = 60 * 1000;

    private AccountService accountService;
    private WebSocketService webSocketService;
    private GameMechanics gameMechanics;

    public GameSocketServelet(AccountService accountService, WebSocketService webSocketService,
                              GameMechanics gameMechanics) {
        this.accountService = accountService;
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(IDLE_TIME);
        webSocketServletFactory.setCreator(new GameSocketCreator(accountService, webSocketService, gameMechanics));
        LOGGER.info("Socket servlet configured");
    }
}
