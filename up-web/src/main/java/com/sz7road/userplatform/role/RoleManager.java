package com.sz7road.userplatform.role;

import com.google.common.collect.MapMaker;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.sz7road.userplatform.playgame.xddt.XddtPlayGameHandler;
import com.sz7road.userplatform.playgame.sq.SqPlayGameHandler2;
import com.sz7road.userplatform.role.RoleHandler;
import com.sz7road.userplatform.role.SqRoleHandler;

import java.util.Map;

/**
 * Author: jiangfan.zhou
 */
@Singleton
public final class RoleManager {

    protected static final int RADIX = 3;

    private final Map<Integer,Provider<? extends RoleHandler>> handlers = new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(8).makeMap();

    @Inject
    public RoleManager(final Injector injector) {
        add(1,injector.getProvider(SqRoleHandler.class));
        add(2,injector.getProvider(XddtRoleHandler.class));
        add(3,injector.getProvider(DdtRoleHandler.class));
        add(4,injector.getProvider(HsRoleHandler.class));
    }

    public RoleHandler get(final int hash){
        return handlers.get(hash).get();
    }

    protected void add(final int code,final Provider<? extends RoleHandler> handler){
        if (handlers.containsKey(code)) throw new IllegalArgumentException("存在相同的游戏键：" + code);
        if (null == handler) throw new NullPointerException("无效的进入游戏处理器。");
        handlers.put(code,handler);
    }

    // Make hash result for the N type.
    protected static int hash(int m, int n) {
        return (m << RADIX) | n;
    }
}
