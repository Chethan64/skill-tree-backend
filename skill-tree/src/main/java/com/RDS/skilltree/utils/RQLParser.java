package com.RDS.skilltree.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RQLParser {
    static Logger logger = LoggerFactory.getLogger(RQLParser.class);
    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> parsedQueryParams = new HashMap<>();

        logger.debug(queryString);

        if (queryString == null || queryString.isEmpty()) {
            return parsedQueryParams;
        }
        //todo exception handling at all levels with custom classes
//        Pattern pattern = Pattern.compile("([^:]+):([^ ]*)\\+?");
        Pattern pattern = Pattern.compile("([^:]+):([^\\s]+)\\s*");

        Matcher matcher = pattern.matcher(queryString);
        while(matcher.find()){
            String key = matcher.group(1);
            String value = matcher.group(2);
            parsedQueryParams.put(key,value);
        }

        logger.debug(parsedQueryParams.toString());

        return parsedQueryParams;
    }
}
