package com.mrcllw.tasklist.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.impl.FacebookTemplate;

import java.io.IOException;
import java.util.HashMap;

public class FacebookUtils {

    public static String GRAPH_API_URL = "https://graph.facebook.com/";
    public static String PICTURE_PARAMS = "/picture?type=large&width=240&height=240";

    public static HashMap getUserDataFromFacebook(String facebookToken){
        Facebook facebook  = new FacebookTemplate(facebookToken);
        String[] fields = {"id", "name", "email"};

        try {
            String userData = facebook.fetchObject("me", String.class, fields);
            return new ObjectMapper().readValue(userData, HashMap.class);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
