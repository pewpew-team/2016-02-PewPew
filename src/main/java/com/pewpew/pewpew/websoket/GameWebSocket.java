package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.GameChanges;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.mechanics.GameMechanics;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.jetbrains.annotations.NotNull;


import java.io.IOException;

@SuppressWarnings("ALL")
@WebSocket
public class GameWebSocket {
    static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    private final String userName;
    private Session userSession;

    @NotNull
    private final GameFrameHandler messageHandler;
    @NotNull
    final WebSocketService webSocketService;
    @NotNull
    final GameMechanics gameMechanics;

    public GameWebSocket(String userName, @NotNull WebSocketService webSocketService, @NotNull GameMechanics gameMechanics) {
        this.userName = userName;
        this.webSocketService = webSocketService;
        this.gameMechanics = gameMechanics;
        this.messageHandler = new GameFrameHandler(gameMechanics);
    }


    @OnWebSocketConnect
    public void onOpen(Session session) {
        userSession = session;
        webSocketService.addUser(this, userName);
        gameMechanics.addUser(userName);
        LOGGER.info("onOpen");
        System.out.println("open websocket");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("closing websocket");
        this.userSession = null;
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        final GameChanges gameFrame;
        try {
            gameFrame = new Gson().fromJson(message, GameChanges.class);
        } catch (JsonSyntaxException ex) {
            LOGGER.error("wrong json format at response", ex);
            return;
        }
        if (gameFrame.getBullet() != null) {
            System.out.println("I got message:" + gameFrame.getBullet());
        }
        final String enemyId = gameMechanics.getEnemy(userName);
        messageHandler.handle(gameFrame, enemyId);
    }

    public void sendMessage(String message) {
        try {
            this.userSession.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startGame() {
        try {
            final JsonObject jsonStart = new JsonObject();
            jsonStart.addProperty("startGame", true);
            if (userSession!= null && userSession.isOpen())
                userSession.getRemote().sendString(jsonStart.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    public void gameOver(boolean win) {
        try {
            final JsonObject jsonEndGame = new JsonObject();
            jsonEndGame.addProperty("status", "finish");
            jsonEndGame.addProperty("win", win);
            if (userSession != null && userSession.isOpen())
                //noinspection ConstantConditions
                userSession.getRemote().sendString(jsonEndGame.toString());
        } catch (IOException | WebSocketException e) {
            LOGGER.error("Can't send web socket", e);
        }
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }
}
