package com.boxfox.support.data;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Config {
    private static Map<String, Config> configMap = new HashMap<>();
    private Properties preference;

    private Config(String name) {
        this.preference = new Properties();
        try {
            preference.load(new FileInputStream(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class DefaultConfig {
        private static Config instance = new Config("config.properties");
    }

    public static Config getDefaultInstance() {
        return DefaultConfig.instance;
    }

    public static Config getInstance(String name) {
        Config config = configMap.get(name);
        if (config == null) {
            config = new Config(name);
            configMap.put(name, config);
        }
        return config;
    }

    public String getString(String propName) {
        return preference.getProperty(propName);
    }

    public String getString(String propName, String defaultStr) {
        String str = preference.getProperty(propName);
        if (str == null) {
            str = defaultStr;
        }
        return str;
    }


    public int getInt(String propName) {
        return Integer.valueOf(preference.getProperty(propName));
    }

    public boolean getBoolean(String propName) {
        return Boolean.valueOf(preference.getProperty(propName));
    }
}
