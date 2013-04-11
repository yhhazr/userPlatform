package com.sz7road.configuration;

/**
 * Just a provider of configuration, no write enabled.
 *
 * @author jeremy
 */
public interface ConfigurationProvider {

    /**
     * Retrieves the value of the specified key.
     *
     * @param key the specified key
     * @return the specified value
     */
    String get(String key);

    /**
     * Tells that whether contains the value with the specified key.
     *
     * @param key the specified key
     * @return true if contains it's, false otherwise
     */
    boolean containsKey(String key);

}
