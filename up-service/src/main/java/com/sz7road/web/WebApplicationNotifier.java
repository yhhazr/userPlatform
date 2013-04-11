package com.sz7road.web;

import com.google.common.collect.Sets;
import com.sz7road.userplatform.core.Injection;
import com.sz7road.userplatform.core.InjectionProxy;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * @author jeremy
 */
@Singleton
public class WebApplicationNotifier {

    @Inject
    private Injection injection;

    /**
     * @author jeremy
     */
    public interface WebApplicationListener {

        void onUnDeploy(InjectionProxy injection);

        void onDeploy(InjectionProxy injection);
    }

    private final Set<WebApplicationListener> listeners = Sets.newHashSet();

    public void notifyUnDeploy() {
        for (WebApplicationListener l : listeners) {
            try {
                l.onUnDeploy(injection);
            } catch (final Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public void notifyDeploy() {
        for (WebApplicationListener l : listeners) {
            try {
                l.onDeploy(injection);
            } catch (final Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public void addListener(WebApplicationListener listener) {
        listeners.add(listener);
    }

    public void removeListener(WebApplicationListener listener) {
        listeners.remove(listener);
    }
}
