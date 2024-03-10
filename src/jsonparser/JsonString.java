package jsonparser;

public class JsonString implements JsonElement {
    private String value;
    public JsonString(String value) {
        this.value = value;
    }
    @Override
    public Object getValue() {
        return value;
    }
}
