/*
 * Copyright (c) 2012. SZ7thRoad.TechSupport All Right Reserved.
 */

package com.sz7road.userplatform.pojos;

import com.google.common.base.Strings;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author jeremy
 */
public class SimpleResponseResult implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(SimpleResponseResult.class.getName());

    public enum ResponseState {

        SUCCESS(1),
        FAILURE(-1),
        INVALID(-2),
        ERROR(500),
        BLOCKED(403);

        private final int code;
        private String str;

        private ResponseState(final int code) {
            this.code = code;
        }

        private ResponseState(final int code, final String value) {
            this.code = code;
            this.str = value;
        }

        public int getCode() {
            return code;
        }

        @Override
        public String toString() {
            if (Strings.isNullOrEmpty(str)) {
                return super.toString().toLowerCase();
            }
            return str;
        }

        public static ResponseState valueOf(int code) {
            for (ResponseState rs : values()) {
                if (rs.code == code) {
                    return rs;
                }
            }

            throw new IllegalArgumentException("Illegal code: " + code + " for ResponseState.");
        }
    }

    private final ResponseState state;
    private Object data;

    public SimpleResponseResult(ResponseState state) {
        this.state = state;
    }

    public SimpleResponseResult(ResponseState state, Object data) {
        this(state);
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object value) {
        this.data = value;
    }

    public ResponseState getResponseState() {
        return state;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(state.toString());
        if (null != data) {
            sb.append('=');

            if (data instanceof String) {
                sb.append(data.toString());
            } else if (data instanceof Serializable) {
                // TODO: Serialize to JSON format.
                ObjectMapper mapper = new ObjectMapper();
                try {
                    sb.append(mapper.writeValueAsString(data));
                } catch (IOException e) {
                    log.error("Constructs the SimpleResponseState toString failure: {}", e.getMessage());
                }
            }
        }
        return sb.toString();
    }

}
