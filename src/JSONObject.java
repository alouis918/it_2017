

import java.util.*;

/**
 * Created by Louis on 08-May-17.
 */
public class JSONObject implements IJsonSerialize {
    private HashMap<String, Object> jsonData = new HashMap<>();

    @Override
    public void addString(String key, String str) {
        if (!jsonData.containsKey(key)){
            jsonData.put(key, str);
        }else{

        }

    }

    @Override
    public void addInteger(String key, int num) {
        if(!jsonData.containsKey(key)){
            jsonData.put(key, num);
        }
    }

    @Override
    public void addDouble(String key, double num) {
        if(!jsonData.containsKey(key)){
            jsonData.put(key, num);
        }
    }

    @Override
    public void addArray(String key, Map<String, Object> array) {
        ArrayList<Object> data = (ArrayList<Object>) array.values();
        Object[] obj = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            obj[i] = data.get(i);
        }
        jsonData.put(key, obj);
    }

    @Override
    public String getString() {
        return null;
    }

    @Override
    public void parseString(String str) {

    }

    @Override
    public Map<String, Object> getObjects() {
        return null;
    }

    @Override
    public Object getKey(String key) {
        return jsonData.get(key);
    }
}
