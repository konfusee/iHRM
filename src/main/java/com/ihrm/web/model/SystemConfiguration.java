package com.ihrm.web.model;

import java.io.Serializable;

public class SystemConfiguration implements Serializable {
    private static final long serialVersionUID = 1L;

    private String configKey;
    private String configValue;

    public SystemConfiguration() {}

    public SystemConfiguration(String configKey, String configValue) {
        this.configKey = configKey;
        this.configValue = configValue;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    @Override
    public String toString() {
        return "SystemConfiguration{" +
                "configKey='" + configKey + '\'' +
                ", configValue='" + configValue + '\'' +
                '}';
    }
}