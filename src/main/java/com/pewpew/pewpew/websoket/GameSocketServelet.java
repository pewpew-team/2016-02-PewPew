package com.pewpew.pewpew.websoket;

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

    public GameSocketServelet(WebSocketService webSocketService,
                              GameMechanics gameMechanics) {
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(IDLE_TIME);
        webSocketServletFactory.setCreator(new GameSocketCreator(webSocketService, gameMechanics));
        LOGGER.info("Socket servlet configured");
    }
}
