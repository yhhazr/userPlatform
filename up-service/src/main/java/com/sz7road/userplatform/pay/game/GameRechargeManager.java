
package com.sz7road.userplatform.pay.game;

import com.google.common.collect.MapMaker;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

@Singleton
public final class GameRechargeManager {

    private static final Logger log = LoggerFactory.getLogger(GameRechargeManager.class.getName());
    private final Map<Integer,Provider<? extends GameRechargeHandler>> handlers = new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(8).makeMap();

    @Inject
    private GameRechargeManager(final Injector injector) {
        handlers.put(1, injector.getProvider(SqChargeHandler.class));
        handlers.put(2, injector.getProvider(XddtChargeHandler.class));
        handlers.put(3, injector.getProvider(DdtChargeHandler.class));
        handlers.put(4, injector.getProvider(HsChargeHandler.class));
    }

    public GameRechargeHandler get(int gameId) {
        if (handlers.containsKey(gameId)) {
            return handlers.get(gameId).get();
        }
        return null;
    }

}
