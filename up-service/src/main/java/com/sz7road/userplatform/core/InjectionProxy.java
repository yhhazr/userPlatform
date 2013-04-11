package com.sz7road.userplatform.core;

import com.google.inject.Key;

import javax.inject.Provider;
import java.lang.annotation.Annotation;

/**
 * @author jeremy
 */
public interface InjectionProxy {

    <T> T getInstance(Class<T> type);

    <T> T getInstance(Class<T> type, String named);

    <T> T getInstance(Class<T> type, Annotation anno);

    <T> T getInstance(Key<T> key);

    <T> Provider<T> getProvider(Class<T> type);

    <T> Provider<T> getProvider(Class<T> type, String named);

    <T> Provider<T> getProvider(Class<T> type, Annotation anno);

    <T> Provider<T> getProvider(Key<T> key);
}
