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
    private static final int IDLE_TIME = 600 * 1000;

    private final WebSocketService webSocketService;
    private final GameMechanics gameMechanics;
    private final AccountService accountService;

    public GameSocketServelet(WebSocketService webSocketService,
                              GameMechanics gameMechanics,
                              AccountService accountService) {
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
        this.accountService = accountService;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(IDLE_TIME);

        webSocketServletFactory.setCreator(new GameSocketCreator(webSocketService, gameMechanics, accountService));
        LOGGER.info("Socket servlet configured");
    }
}
