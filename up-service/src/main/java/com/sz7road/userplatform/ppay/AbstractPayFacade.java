package com.sz7road.userplatform.ppay;

import com.google.common.collect.MapMaker;

import javax.inject.Provider;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author jeremy
 */
public abstract class AbstractPayFacade {

    protected static final String CHANNEL_KEY = "_c";
    protected static final String SUB_TYPE_KEY = "_s";
    protected static final int RADIX = 3;

    private final Map<Integer, Provider<? extends PayHandler>> handlers =
            new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(8).makeMap();

    // Make hash result for the N type.
    protected static int hash(int m, int n) {
        int hashResult= (m << RADIX) | n;
        return hashResult;
    }

    // Make hash result for the N type.
    protected static int hash(final HttpServletRequest request) {
        final int channel = Integer.parseInt(request.getParameter(CHANNEL_KEY).trim());
        final int subType = Integer.parseInt(request.getParameter(SUB_TYPE_KEY).trim());
        return hash(channel, subType);
    }

    /**
     * Constructs by default.
     */
    public AbstractPayFacade() {
        super();
    }

    protected void add(final int hash, Provider<? extends PayHandler> handler) {
        if (handlers.containsKey(hash)) {
            throw new IllegalArgumentException("存在相同的充值渠道HASH键：" + hash);
        }
        if (null == handler) {
            throw new NullPointerException("无效充值渠道处理器。");
        }

        handlers.put(hash, handler);
    }

    public PayHandler get(final int hash) {
        if (handlers.containsKey(hash)) {
            return handlers.get(hash).get();
        }
        return null;
    }

    /**
     * <b color="red">在调用该方法之前，请务必确保request中存在且不为空的渠道标识参数。</b>
     *
     * @param request 请求对象
     * @return 充值渠道控制器
     */
    public PayHandler get(final HttpServletRequest request) {
        return get(hash(request));
    }
}
