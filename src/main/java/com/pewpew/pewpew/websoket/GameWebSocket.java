package com.pewpew.pewpew.websoket;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

@ServerEndpoint(value = "/ws",
        configurator=MyEndpointConfigurator.class)
public class GameWebSocket {
    static final Logger logger = LogManager.getLogger(GameWebSocket.class);

    private User user;
    private Session userSession;

    @NotNull
    private GameFrameHandler messageHandler;

//    @Inject
//    private Context context;

    @OnOpen
    public void onOpen(Session userSession) {
        this.userSession = userSession;
//        Map<String, Object> userProperties = userSession.getUserProperties();
//        String cookie = ((List<String>)userProperties.get("cookie")).get(0);

        WebSocketService webSocketService = WebSocketServiceImpl.getInstance();
        webSocketService.addUser(this, userSession.getId());

        GameMechanics gameMechanics = GameMechanicsImpl.getInstance();
        gameMechanics.addUser(userSession.getId());

        messageHandler = new GameFrameHandler(webSocketService);
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
            final GameFrame gameFrame;
            try {
                gameFrame = new Gson().fromJson(message, GameFrame.class);
            } catch (JsonSyntaxException ex) {
                logger.error("wrong json format at ping response", ex);
                return;
            }
            try {
                //noinspection ConstantConditions
                messageHandler.handle(gameFrame, userSession.getId());
            } catch (HandleException e) {
                logger.error("Can't handle message with content: " + message, e);
            }
        }
    }

//    public void addMessageHandler(MessageHandler msgHandler) {
//        this.messageHandler = msgHandler;
//    }

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
}
