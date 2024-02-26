package com.RDS.skilltree.utils;

import org.junit.Test;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

public class RQLParserTest {
    @Test
    public void itShouldReturnMapWithCorrectUserId() {
        Map<String, String> parsedQuery = RQLParser.parseQueryString("userId:ff7afdd6-1186-4bc8-a52f-5af4bfc1ddf1");
        assertTrue(parsedQuery.containsKey("userId"));
        assertEquals("ff7afdd6-1186-4bc8-a52f-5af4bfc1ddf1", parsedQuery.get("userId"));
    }

    @Test
    public void itShouldReturnMapWithCorrectSkillId() {
        Map<String, String> parsedQuery = RQLParser.parseQueryString("skillId:5e47ce0a-5d86-4fe0-b53d-8d18939f1274");
        assertTrue(parsedQuery.containsKey("skillId"));
        assertEquals("5e47ce0a-5d86-4fe0-b53d-8d18939f1274", parsedQuery.get("skillId"));
    }

    @Test
    public void itShouldReturnEmptyMapIfQueryStringIsEmpty() {
        Map<String, String> parsedQuery = RQLParser.parseQueryString("");
        assertTrue(parsedQuery.isEmpty());
    }

    @Test
    public void itShouldReturnEmptyIfQueryStringDoesNotContainValue() {
        Map<String, String> parsedQuery = RQLParser.parseQueryString("userID:");
        assertTrue(parsedQuery.isEmpty());
    }

    @Test
    public void itShouldReturnMapWithBothUserIDAndSkillID() {
        Map<String, String> parsedQuery = RQLParser.parseQueryString("userID:5e47ce0a-5d86-4fe0-b53d-8d18939f1274+skillID:5e47ce0a-5d86-4fe0-b53d-8d18939f1274");
        assertTrue(parsedQuery.containsKey("userID"));
        assertTrue(parsedQuery.containsKey("skillID"));
        assertEquals("5e47ce0a-5d86-4fe0-b53d-8d18939f1274", parsedQuery.get("userID"));
        assertEquals("5e47ce0a-5d86-4fe0-b53d-8d18939f1274", parsedQuery.get("skillID"));
    }
}