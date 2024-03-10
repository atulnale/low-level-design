package jsonparser;

import java.util.Map;

public class JsonObject implements JsonElement{
        Map<String, JsonElement> properties;

    public JsonObject(Map<String, JsonElement> properties) {
        this.properties = properties;
    }

    @Override
    public Object getValue() {
        return properties;
    }
}
