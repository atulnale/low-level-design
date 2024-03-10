package jsonparser;

public class JsonBoolean implements JsonElement{
    private boolean value;
    public JsonBoolean(boolean value){
        this.value = value;
    }

    @Override
    public Object getValue() {
        return value;
    }
}
