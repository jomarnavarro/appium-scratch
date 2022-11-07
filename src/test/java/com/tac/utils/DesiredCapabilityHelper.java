package com.tac.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DesiredCapabilityHelper {

    private static final String JSON_CAPS_FILE_FOLDER = "src/test/resources/capabilities";
    private static final String DEFAULT_DESIRED_CAPS_JSON_FILE = "capabilityList.json";

    private static JSONArray parseJSON(String jsonLocation) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        return (JSONArray) jsonParser.parse(new FileReader(jsonLocation));
    }

    private static JSONObject getCapability(String capabilityName, String jsonLocation) throws IOException, ParseException {
        JSONArray capabilitiesArray = JSONUtils.parseJsonArrayFile(jsonLocation);
        for (Object jsonObj : capabilitiesArray) {
            JSONObject capability = (JSONObject) jsonObj;
            if (capability.get("name").toString().equalsIgnoreCase(capabilityName)) {
                return (JSONObject) capability.get("caps");
            }
        }
        return null;
    }

    private static HashMap<String, Object> convertCapsToHashMap(String capabilityName, String jsonLocation) throws IOException, ParseException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(getCapability(capabilityName, jsonLocation).toString(), HashMap.class);
    }

    public static DesiredCapabilities getDesiredCapabilities(String capabilityName, String capsContentRootLocation) throws IOException, ParseException {
        String jsonLocation = String.format("%s/%s/%s", System.getProperty("user.dir"), JSON_CAPS_FILE_FOLDER, capsContentRootLocation);
        HashMap<String, Object>  caps = convertCapsToHashMap(capabilityName, jsonLocation);
        return new DesiredCapabilities(caps);
    }

    public static DesiredCapabilities getDesiredCapabilities(String capabilityName) throws IOException, ParseException {
        return getDesiredCapabilities(capabilityName, DEFAULT_DESIRED_CAPS_JSON_FILE);
    }
}
