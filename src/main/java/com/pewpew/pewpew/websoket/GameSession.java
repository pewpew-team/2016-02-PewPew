package com.pewpew.pewpew.websoket;

import javax.websocket.Session;

public class GameSession {
    WebSocketStart playerOne;
    WebSocketStart playerTwo;

    public GameSession(WebSocketStart playerOne, WebSocketStart playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Boolean contains(Session session) {
        if (playerOne.getUserSession().equals(session) ||
                playerTwo.getUserSession().equals(session) ) {
            return true;
        }
        return false;
    }
}
