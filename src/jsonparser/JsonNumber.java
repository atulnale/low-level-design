package jsonparser;

public class JsonNumber implements JsonElement {
    private Number value;
    public JsonNumber(Number value) {
        this.value = value;
    }
    @Override
    public Object getValue() {
        return value;
    }
}
