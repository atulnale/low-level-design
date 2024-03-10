package jsonparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {
    private int index;
    private String json;

    private static final char OPEN_CURLY_BRACE = '{';
    private static final char CLOSE_CURLY_BRACE = '}';
    private static final char OPEN_SQUARE_BRACKET = '[';
    private static final char CLOSE_SQUARE_BRACKET = ']';
    private static final char DOUBLE_QUOTE = '"';
    private static final char COLON = ':';
    private static final char COMMA = ',';

    public JsonElement parse(String jsonString) {
        this.index = 0;
        this.json = jsonString;
        skipWhitespaces();
        return parseValue();
    }

    private JsonElement parseValue() {
        char currentChar = json.charAt(index);
        if(currentChar == OPEN_CURLY_BRACE){
            return parseObject();
        } else if (currentChar == OPEN_SQUARE_BRACKET) {
            return parseArray();
        } else if (currentChar == DOUBLE_QUOTE) {
            return parseString();
        } else if (Character.isDigit(currentChar) || currentChar == '-') {
            return parseNumber();
        } else if (currentChar == 't' || currentChar == 'f') {
            return parseBoolean();
        } else if (currentChar == 'n') {
            return parseNull();
        }

        throw new RuntimeException("Invalid JSON");

    }

    private JsonElement parseNull() {
        consumeWord(); // Consume "null"
        return null;
    }

    private JsonElement parseBoolean() {
        String boolStr = consumeWord();
        if (boolStr.equals("true")) {
            return new JsonBoolean(true);
        } else if (boolStr.equals("false")) {
            return new JsonBoolean(false);
        }

        throw new RuntimeException("Invalid boolean value");
    }
    private String consumeWord() {
        StringBuilder sb = new StringBuilder();
        while (Character.isLetter(json.charAt(index))) {
            sb.append(json.charAt(index));
            index++;
        }
        return sb.toString();
    }

    private JsonElement parseNumber() {
        int startIndex = index;

        // Consume digits and optional decimal point
        while (Character.isDigit(json.charAt(index)) || json.charAt(index) == '.') {
            index++;
        }

        String numberStr = json.substring(startIndex, index);
        if (numberStr.contains(".")) {
            return new JsonNumber(Double.parseDouble(numberStr));
        } else {
            return new JsonNumber(Long.parseLong(numberStr));
        }
    }

    private JsonElement parseString() {
        consume(DOUBLE_QUOTE);
        StringBuilder builder = new StringBuilder();
        while(json.charAt(index) != DOUBLE_QUOTE){
            builder.append(json.charAt(index));
            index++;
        }
        consume(DOUBLE_QUOTE);
        return new JsonString(builder.toString());
    }

    private JsonElement parseArray() {
        List<JsonElement> elements = new ArrayList<>();
        consume(OPEN_SQUARE_BRACKET);
        skipWhitespaces();
        while(json.charAt(index) != CLOSE_SQUARE_BRACKET){
            skipWhitespaces();
            JsonElement jsonElement = parseValue();
            elements.add(jsonElement);
            skipWhitespaces();
            if(json.charAt(index) == COMMA){
                consume(COMMA);
                skipWhitespaces();
            }
        }
        consume(CLOSE_SQUARE_BRACKET);
        return new JsonArray(elements);
    }

    private JsonElement parseObject() {
        Map<String, JsonElement> properties = new HashMap<>();
        consume(OPEN_CURLY_BRACE);
        skipWhitespaces();
        while(json.charAt(index) != CLOSE_CURLY_BRACE){
            String propertyName = parseString().getValue().toString();
            skipWhitespaces();
            consume(COLON);
            skipWhitespaces();
            JsonElement propertyValue = parseValue();
            properties.put(propertyName, propertyValue);
            skipWhitespaces();
            if(json.charAt(index) == COMMA){
                consume(COMMA);
                skipWhitespaces();
            }
        }
        consume(CLOSE_CURLY_BRACE);
        return new JsonObject(properties);
    }

    private void consume(char expected) {
        if(json.charAt(index) == expected){
            index++;
        } else {
            throw new RuntimeException("Expected: "+ expected);
        }
    }

    public void skipWhitespaces(){
        while(Character.isWhitespace(json.charAt(index))){
            index++;
        }
    }

    public static void main(String[] args) {
        String jsonString = "{ \"name\": \"John\", \"age\": 30, \"city\": \"New York\", \"isAdmin\": true, \"scores\": [10, 20, 30] }";

        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(jsonString);

        System.out.println(jsonElement.getValue());
    }
}
