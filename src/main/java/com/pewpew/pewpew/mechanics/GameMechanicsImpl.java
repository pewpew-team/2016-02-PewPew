package com.pewpew.pewpew.mechanics;

import com.google.gson.Gson;
import com.pewpew.pewpew.messageSystem.Abonent;
import com.pewpew.pewpew.messageSystem.Address;
import com.pewpew.pewpew.messageSystem.MessageSystem;
import com.pewpew.pewpew.messages.toWebSocket.MessageGameOver;
import com.pewpew.pewpew.messages.toWebSocket.MessageGameStart;
import com.pewpew.pewpew.messages.toWebSocket.MessageToUser;
import com.pewpew.pewpew.model.Bullet;
import com.pewpew.pewpew.model.GameChanges;
import com.pewpew.pewpew.model.GameFrame;
import com.pewpew.pewpew.model.PlayerObject;
import org.eclipse.jetty.websocket.api.Session;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Clock;
import java.util.*;

public class GameMechanicsImpl implements GameMechanics, Abonent, Runnable {

    private final Double yMax;
    private final Double xMax;

    private final long stepTime;

    private final Address address = new Address();
    private final MessageSystem messageSystem;

    @NotNull
    private final Set<GameSession> allSessions = new HashSet<>();

    @NotNull
    private final Map<String, GameSession> nameToGame = new HashMap<>();

    @NotNull
    private final Map<String, Address> addressMap = new HashMap<>();

    @Nullable
    private volatile String waiter;

    @NotNull
    private final Gson gson;

    @NotNull
    private final Clock clock = Clock.systemDefaultZone();

    public GameMechanicsImpl(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
        messageSystem.getAddressService().registerGameMechanics(this);

        this.gson = new Gson();

        final Properties property = new Properties();
        final String path = new File("").getAbsolutePath() + "/resources/game.properties";
        try (FileInputStream fileInputStream =
                     new FileInputStream(path)) {
            property.load(fileInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Can't handle game resourses");
        }
        stepTime = Integer.valueOf(property.getProperty("game.stepTime"));
        xMax = Double.valueOf(property.getProperty("map.xMax"));
        yMax = Double.valueOf(property.getProperty("map.yMax"));
    }

    @Override
    public void addUser(@NotNull String user, @NotNull Address addressSocket) {
        addressMap.put(user, addressSocket);
        if (!recoverGame(user)) {
            if (waiter != null) {
                //noinspection ConstantConditions
                startGame(user, waiter);
                waiter = null;
            } else {
                waiter = user;
            }
        }
    }

    @Override
    public String getEnemy(@NotNull String user) {
        if (!Objects.equals(nameToGame.get(user).getPlayerTwo(), user)) {
            return nameToGame.get(user).getPlayerTwo();
        }
        return nameToGame.get(user).getPlayerOne();
    }

    public void removeSession(Session session) {
        allSessions.remove(session);
    }

    public void pauseGame(String dissconectedUser) {
        GameSession gameSession = nameToGame.get(dissconectedUser);
        gameSession.paused = true;
    }

    @NotNull
    private Boolean recoverGame(String returnedUser) {
        final Boolean[] isReturned = {false};
        allSessions.stream().filter(session -> session.paused).forEach(session -> {
            if (session.getPlayerOne().equals(returnedUser) ||
                    session.getPlayerTwo().equals(returnedUser)) {
                session.paused = false;
                isReturned[0] = true;
            }
        });
        return isReturned[0];
    }

    @Override
    public void start() {
        Thread gameMechanicsThread = (new Thread(this));
        gameMechanicsThread.setDaemon(true);
        gameMechanicsThread.setName("Game Mechanics");
        gameMechanicsThread.run();
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        long lastFrameMilles = stepTime;
        while (true) {
            final long before = clock.millis();
            gameStep(lastFrameMilles);
            final long after = clock.millis();
            if (after - before > stepTime) {
                System.out.println("gm is lagging. step is " + (after - before) + "ms");
            }
            try {
                final long lag =  after - before;
                if (stepTime - lag > 0) {
                    Thread.sleep(stepTime - lag);
                }
                //Thread.sleep(stepTime - (after - before));
                final long afterSleep = clock.millis();
                lastFrameMilles = afterSleep - before;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void gameStep(long timeTick) {
        messageSystem.execForAbonent(this);
        allSessions.stream().filter(session -> !session.paused).forEach(session -> {
            sendState(session, timeTick);
            if (session.getPlayerOneWon() != null) {
                final Boolean firstWin = session.getPlayerOneWon();

                Address firstUserAddress = addressMap.get(session.getPlayerOne());
                MessageGameOver messageGameOver = new MessageGameOver(address, firstUserAddress, firstWin);
                messageSystem.sendMessage(messageGameOver);

                Address secondUserAddress = addressMap.get(session.getPlayerTwo());
                messageGameOver = new MessageGameOver(address, secondUserAddress, firstWin);
                messageSystem.sendMessage(messageGameOver);
            }
        });
    }

    private void startGame(@NotNull String first, @NotNull String second) {
        final GameSession gameSession = new GameSession(first, second, xMax, yMax);
        allSessions.add(gameSession);

        nameToGame.put(first, gameSession);
        nameToGame.put(second, gameSession);

        Address firstUserAddress = addressMap.get(gameSession.getPlayerOne());
        MessageGameStart messageGameStart = new MessageGameStart(address, firstUserAddress, gameSession.getPlayerTwo());
        messageSystem.sendMessage(messageGameStart);

        Address secondUserAddress = addressMap.get(gameSession.getPlayerTwo());
        messageGameStart = new MessageGameStart(address, secondUserAddress, gameSession.getPlayerOne());
        messageSystem.sendMessage(messageGameStart);
    }


    public void sendState(GameSession gameSession, long timeTick) {
        if (gameSession != null) {
            final GameFrame gameFrame = gameSession.getGameFrame();
            gameSession.moveBullets(timeTick);

            Address firstUserAddress = addressMap.get(gameSession.getPlayerOne());
            final String gameFrameJson = gson.toJson(gameFrame);
            MessageToUser messageToUser = new MessageToUser(address, firstUserAddress, gameFrameJson);
            messageSystem.sendMessage(messageToUser);

            PlayerObject player = gameFrame.getEnemy();
            gameFrame.setEnemy(gameFrame.getPlayer());
            gameFrame.setPlayer(player);
            gameFrame.toAnotherCoordinateSystem(xMax, yMax);

            final String gameFrameJsonSecond = gson.toJson(gameFrame);
            Address secondUserAddress = addressMap.get(gameSession.getPlayerTwo());
            MessageToUser secondMessageToUser = new MessageToUser(address, secondUserAddress, gameFrameJsonSecond);
            messageSystem.sendMessage(secondMessageToUser);

            player = gameFrame.getEnemy();
            gameFrame.setEnemy(gameFrame.getPlayer());
            gameFrame.setPlayer(player);
            gameFrame.toAnotherCoordinateSystem(xMax, yMax);
        }
    }

    @Override
    public void changeState(GameChanges gameChanges, String userName) {
        final GameSession gameSession = nameToGame.get(userName);
        if (gameSession != null) {
            final PlayerObject playerObject = gameChanges.getPlayer();
            final GameFrame gameFrame = gameSession.getGameFrame();
            if (gameSession.getPlayerOne().equals(userName)) {
                final Bullet bullet = gameChanges.getBullet();
                if (bullet != null) {
                    bullet.toAnotherCoordinateSystem(xMax, yMax);
                    gameChanges.setBullet(bullet);
                }
                if (playerObject != null) {
                    playerObject.toAnotherCoordinateSystem(xMax);
                    playerObject.translateGunAgnle();
                    gameFrame.setEnemy(playerObject);
                }
            } else {
                if (playerObject != null) {
                    playerObject.translateGunAgnle();
                    gameFrame.setPlayer(playerObject);
                }
            }
            gameSession.changeState(gameChanges);
        }
    }


    @Override
    public void closeGameSession(String user) {
        final GameSession gameSession = nameToGame.remove(user);
        if (gameSession != null) {
            nameToGame.remove(gameSession.getPlayerTwo());
            allSessions.remove(gameSession);
        } else {
            waiter = null;
        }
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }
}
