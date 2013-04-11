package com.sz7road.userplatform.playgame;

import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.playgame.ddt.DdtPlayGameHandler;
import com.sz7road.userplatform.playgame.xddt.XddtPlayGameHandler;
import com.sz7road.userplatform.playgame.sq.SqPlayGameHandler2;

import java.util.Map;

/**
 * User: leo.liao
 * Date: 12-6-5
 * Time: 下午12:06
 */
@Singleton
public final class PlayGameManager {

    protected static final int RADIX = 3;

    private final Map<Integer,Provider<? extends PlayGameHandler>> handlers = new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(8).makeMap();

    @Inject
    public PlayGameManager(final Injector injector) {
        add(hash('S',0),injector.getProvider(SqPlayGameHandler2.class));
        add(hash('X',0),injector.getProvider(XddtPlayGameHandler.class));
        add(hash('D',0),injector.getProvider(DdtPlayGameHandler.class));
        //add(hash('H',0),injector.getProvider(HsPlayGameHandler.class));
    }

    public PlayGameHandler get(final int hash){
        return handlers.get(hash).get();
    }

    protected void add(final int code,final Provider<? extends PlayGameHandler> handler){
        if (handlers.containsKey(code)) throw new IllegalArgumentException("存在相同的游戏键：" + code);
        if (null == handler) throw new NullPointerException("无效的进入游戏处理器。");
        handlers.put(code,handler);
    }

    // Make hash result for the N type.
    protected static int hash(int m, int n) {
        return (m << RADIX) | n;
    }
}
