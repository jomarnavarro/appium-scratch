package com.tac.utils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONUtils {

    private static JSONParser parser;

    public static JSONObject parseJsonObject(String jsonTxt) throws ParseException {
        parser = new JSONParser();
        return (JSONObject) parser.parse(jsonTxt);
    }

    public static JSONArray parseJsonArray(String jsonTxt) throws ParseException {
        parser = new JSONParser();
        return (JSONArray) parser.parse(jsonTxt);
    }

    public static JSONArray parseJsonArrayFile(String location) throws IOException, ParseException {
        parser = new JSONParser();
        return (JSONArray) parser.parse(new FileReader(location));
    }
}
