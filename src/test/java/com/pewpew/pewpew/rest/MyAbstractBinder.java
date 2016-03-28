package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

public class MyAbstractBinder extends AbstractBinder {
    private final Context context;

    MyAbstractBinder(Context context) {
        this.context = context;
    }

    @Override
    protected void configure() {
        bind(context);
    }
}