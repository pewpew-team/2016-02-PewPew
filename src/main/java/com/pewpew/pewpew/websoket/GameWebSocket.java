package com.pewpew.pewpew.websoket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.JsonObject;
import com.pewpew.pewpew.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.WebSocketException;


import java.io.IOException;
import java.util.Map;

@ServerEndpoint(value = "/wb",
        configurator=MyEndpointConfigurator.class)
public class GameWebSocket {
    static final Logger logger = LogManager.getLogger(GameWebSocket.class);

    private User user;
    private Session userSession;
    private MessageHandler messageHandler;

//    @Inject
//    private Context context;

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        Map<String, Object> userProperties = userSession.getUserProperties();
        Object cookie = userProperties.get("cookie");

        WebSocketService webSocketService = WebSocketServiceImpl.getInstance();
        webSocketService.addUser(this, "user");

        GameMechanics gameMechanics = GameMechanicsImpl.getInstance();
        gameMechanics.addUser("user");
        logger.info("onOpen");
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            this.messageHandler.handleMessage(message);
            logger.info("Got message: " + message);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void sendMessage(String message) {
        this.userSession.getAsyncRemote().sendText(message);
    }

    public void startGame() {
        try {
            final JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("startGame", true);
            if (userSession!= null && userSession.isOpen())
                userSession.getBasicRemote().sendText(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            logger.error("Can't send web socket", e);
        }
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    public static interface MessageHandler {

        public void handleMessage(String message);
    }
}
