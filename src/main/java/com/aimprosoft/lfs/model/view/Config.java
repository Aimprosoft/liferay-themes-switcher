package com.aimprosoft.lfs.model.view;

import com.aimprosoft.lfs.model.persist.ConfigKey;

/**
 * @author AimProSoft created on 24.06.16 10:21
 */
public class Config {
    private ConfigKey key;
    private String value;

    public ConfigKey getKey() {
        return key;
    }

    public void setKey(ConfigKey key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
