package jsonparser;

import java.util.List;

public class JsonArray implements JsonElement{
    private List<JsonElement> value;
    public JsonArray(List<JsonElement> value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
