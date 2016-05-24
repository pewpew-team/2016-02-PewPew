package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.messagesystem.Address;
import com.pewpew.pewpew.messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;


@WebServlet(name = "GameSocketServelet", urlPatterns = {"/ws"})
public class GameSocketServelet extends WebSocketServlet {
    static final Logger LOGGER = LogManager.getLogger(GameSocketServelet.class);
    private static final int IDLE_TIME = 600 * 1000;
    private final Address address = new Address();

    private final AccountService accountService;
    private final MessageSystem messageSystem;
    private final Address gameMechanicsAddress;

    public GameSocketServelet(AccountService accountService,
                              MessageSystem messageSystem,
                              Address gameMechanicsAddress) {
        this.accountService = accountService;
        this.messageSystem = messageSystem;
        this.gameMechanicsAddress = gameMechanicsAddress;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.getPolicy().setIdleTimeout(IDLE_TIME);

        webSocketServletFactory.setCreator(new GameSocketCreator(accountService, messageSystem, gameMechanicsAddress));
        LOGGER.info("Socket servlet configured");
    }

}
