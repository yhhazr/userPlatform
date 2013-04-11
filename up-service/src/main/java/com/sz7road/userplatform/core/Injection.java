/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.core;

import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;

import javax.inject.Inject;
import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * @author jeremy
 */
public class Injection implements InjectionProxy {

    @Inject
    private Injector injector;

    @Override
    public <T> T getInstance(final Class<T> type) {
        if (null != type) {
            return injector.getInstance(type);
        }
        return null;
    }

    @Override
    public <T> T getInstance(final Class<T> type, final String named) {
        return getInstance(type, Names.named(named));
    }

    @Override
    public <T> T getInstance(final Class<T> type, final Annotation anno) {
        return getInstance(Key.get(type, anno));
    }

    @Override
    public <T> T getInstance(final Key<T> key) {
        if (null != key) {
            return injector.getInstance(key);
        }
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(final Class<T> type) {
        if (null != type) {
            return injector.getProvider(type);
        }
        return null;
    }

    @Override
    public <T> Provider<T> getProvider(final Class<T> type, final String named) {
        return getProvider(type, Names.named(named));
    }

    @Override
    public <T> Provider<T> getProvider(final Class<T> type, final Annotation anno) {
        return getProvider(Key.get(type, anno));
    }

    @Override
    public <T> Provider<T> getProvider(final Key<T> key) {
        if (null != key) {
            return injector.getProvider(key);
        }
        return null;
    }

}
