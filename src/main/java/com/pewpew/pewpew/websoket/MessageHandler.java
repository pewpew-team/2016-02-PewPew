package com.pewpew.pewpew.websoket;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.model.GameFrame;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Solovyev on 06/04/16.
 */
public abstract class MessageHandler<T> {
    @NotNull
    private final Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    public void handleMessage(@NotNull String gameFrame, @NotNull String forUser) throws HandleException {
        try {
            final Object data = new Gson().fromJson(gameFrame, clazz);

            //noinspection ConstantConditions
            handle(clazz.cast(data), forUser);
        } catch (JsonSyntaxException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message with content: " + gameFrame, ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull String forUser) throws HandleException;
}
