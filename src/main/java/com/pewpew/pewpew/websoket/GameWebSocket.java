package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.messageSystem.Abonent;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.messageSystem.MessageSystem;
import com.pewpew.pewpew.messages.MessageGameChanges;
import com.pewpew.pewpew.messages.MessagePauseGame;
import com.pewpew.pewpew.messages.MessageRegister;
import com.pewpew.pewpew.messages.MessageRemoveSession;
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
public class GameWebSocket implements Abonent,Runnable {
    static final Logger LOGGER = LogManager.getLogger(GameWebSocket.class);

    private final String userName;
    private String enemyName;
    private Session userSession;
    private Boolean gameEnded;
    private final Address address = new Address();;
    private final Address gameMechanicsAddress;

    @NotNull
    private final MessageSystem messageSystem;

    public GameWebSocket(String userName, MessageSystem messageSystem,
                         Address gameMechanicsAddress) {
        this.userName = userName;
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameWebSocket(this);
        this.gameMechanicsAddress = gameMechanicsAddress;
    }


    @OnWebSocketConnect
    public void onOpen(Session session) {
        userSession = session;
        MessageRegister registerMessage = new MessageRegister(address, gameMechanicsAddress, userName);
        messageSystem.sendMessage(registerMessage);
        LOGGER.info("onOpen");
        System.out.println("open websocket");
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("closing websocket");
        if (gameEnded) {
            MessageRemoveSession messageRemoveSession =
                    new MessageRemoveSession(address, gameMechanicsAddress, userName);
            messageSystem.sendMessage(messageRemoveSession);
            gameEnded = false;
        } else {
            MessagePauseGame messagePauseGame = new MessagePauseGame(address, gameMechanicsAddress, userName);
            messageSystem.sendMessage(messagePauseGame);
        }
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
        MessageGameChanges messageGameChanges = new MessageGameChanges(address, gameMechanicsAddress, gameFrame, enemyName);
        messageSystem.sendMessage(messageGameChanges);

    }

    public void sendMessage(String message) {
        if (this.userSession != null && message != null) {
            try {
                this.userSession.getRemote().sendString(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void start() {
        (new Thread(this)).start();
    }

    public void setGameEnded(Boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public Session getUserSession() {
        return userSession;
    }

    public void setUserSession(Session userSession) {
        this.userSession = userSession;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    public void setEnemyName(String enemyName) {
        this.enemyName = enemyName;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            messageSystem.execForAbonent(this);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
