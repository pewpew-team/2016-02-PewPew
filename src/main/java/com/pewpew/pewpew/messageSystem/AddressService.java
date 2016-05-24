package com.pewpew.pewpew.messageSystem;



import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.websoket.GameWebSocket;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author e.shubin
 */
public final class AddressService {
    private Address gameMechanics;
    private Address gameWebSocket;

    private AtomicInteger accountServiceCounter = new AtomicInteger();

    public void registerGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }

    public Address getGameWebSocketAddress() {
        return gameWebSocket;
    }

    public void registerGameWebSocket(GameWebSocket gameWebSocket) {
        this.gameWebSocket = gameWebSocket.getAddress();
    }
}
