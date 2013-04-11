package com.sz7road.configuration;

/**
 * @author leo.liao
 */


public interface FilterCharacterProvider {

    boolean isContainKey(String key);

    boolean refresh();
}
