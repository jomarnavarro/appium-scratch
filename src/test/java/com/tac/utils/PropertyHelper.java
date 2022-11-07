package com.tac.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyHelper {
    private static Properties properties;
    private static final String PROPERTY_FILE_FOLDER = "src/test/resources/properties";
    private static final String DEFAULT_PROPERTY_FILE = "default.properties";

    private static Properties getProperties() {
        if(properties == null) {
            properties = new Properties();
            addPreferences(properties, DEFAULT_PROPERTY_FILE);
            Properties systemProps = (Properties) System.getProperties().clone();
            properties.putAll((Properties) System.getProperties().clone());
            properties.putAll(System.getenv());
            if(properties.getProperty("APPIUM_USER") != null) {
                addPreferences(properties, properties.getProperty("APPIUM_USER")+".properties");
            }
            System.setProperties(properties);
        }
        return properties;
    }

    private static void addPreferences(Properties properties, String propertyFilename) {
        String propertiesFilePath = String.format("%s/%s/%s", System.getProperty("user.dir"), PROPERTY_FILE_FOLDER, propertyFilename);
        try(InputStream inputStream = new FileInputStream(propertiesFilePath)) {
            Properties prop = new Properties();
            prop.load(inputStream);
            properties.putAll(prop);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        return getProperties().getProperty(propertyName);
    }
}
