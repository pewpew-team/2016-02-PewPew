package com.pewpew.pewpew.websoket;

import com.pewpew.pewpew.model.GameFrame;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 06/04/16.
 */
public interface MessageHandlerContainer {

    void handle(@NotNull GameFrame gameFrame, @NotNull String forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
