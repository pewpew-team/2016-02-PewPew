package com.pewpew.pewpew.websoket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.model.GameFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

@WebSocket
public class GameWebSocket {
    static final Logger logger = LogManager.getLogger(GameWebSocket.class);

    private String userName;
    private Session userSession;

    @NotNull
    private GameFrameHandler messageHandler;
    @NotNull
    WebSocketService webSocketService;
    @NotNull
    GameMechanics gameMechanics;

    public GameWebSocket(String userName, WebSocketService webSocketService, GameMechanics gameMechanics) {
        this.userName = userName;
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
    }


    @OnWebSocketConnect
    public void onOpen(Session userSession) {
        this.userSession = userSession;
        webSocketService.addUser(this, userName);
        gameMechanics.addUser(userName);
        messageHandler = new GameFrameHandler(webSocketService, gameMechanics);
        logger.info("onOpen");
        System.out.println("open websocket");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        if (this.messageHandler != null) {
            final GameFrame gameFrame;
            try {
                gameFrame = new Gson().fromJson(message, GameFrame.class);
            } catch (JsonSyntaxException ex) {
                logger.error("wrong json format at response", ex);
                return;
            }
            try {
                if (gameFrame.getBullet() != null) {
                    System.out.print("I got message:" + gameFrame.getBullet() + "\n");
                }
                String enemyId = gameMechanics.getEnemy(userName);
                messageHandler.handle(gameFrame, enemyId);
            } catch (HandleException e) {
                logger.error("Can't handle message with content: " + message + "\n", e);
            }
        }
    }

    public void sendMessage(String message) {
        try {
            this.userSession.getRemote().sendString(message);
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public void startGame() {
        try {
            final JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("startGame", true);
            if (userSession!= null && userSession.isOpen())
                userSession.getRemote().sendString(jsonStart.toString());
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
