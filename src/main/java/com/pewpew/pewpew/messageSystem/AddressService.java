package com.pewpew.pewpew.messageSystem;



import com.pewpew.pewpew.mechanics.GameMechanics;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author e.shubin
 */
public final class AddressService {
    private Address gameMechanics;

    private AtomicInteger accountServiceCounter = new AtomicInteger();

    public void registerGameMechanics(GameMechanics gameMechanics) {
        this.gameMechanics = gameMechanics.getAddress();
    }

    public Address getGameMechanicsAddress() {
        return gameMechanics;
    }

}
