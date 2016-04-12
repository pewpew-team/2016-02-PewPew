package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.GameFrame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Solovyev on 06/04/16.
 */
public class GameFrameHandlerContainer implements MessageHandlerContainer {
    @SuppressWarnings("ConstantConditions")
    @NotNull
    private static final Logger LOGGER = LogManager.getLogger(GameFrameHandlerContainer.class);
    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull GameFrame gameFrame, @NotNull String userName) throws HandleException {

        final Class clazz;
        try {
            clazz = Class.forName(gameFrame.toString());
        } catch (ClassNotFoundException e) {
            throw new HandleException("Can't handle message of " + gameFrame + " type", e);
        }
        MessageHandler<?> messageHandler = handlerMap.get(clazz);
        if (messageHandler == null) {
            throw new HandleException("no handler for message of " + gameFrame + " type");
        }
        messageHandler.handleMessage(gameFrame.toString(), userName);
        LOGGER.info("message handled: type =[" + gameFrame + "], content=[" + gameFrame+ ']');
    }

    @Override
    public <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }
}
