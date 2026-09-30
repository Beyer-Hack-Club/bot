package xyz.pbsi.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;

public class JSON {
    public static String hashMapToJSON(HashMap<String, String> hashMap) {
        Gson gson = new Gson();
        Type typeObject = new TypeToken<HashMap<String, String>>() {}.getType();
        return gson.toJson(hashMap, typeObject);
    }
    public static HashMap<?, ?> JSONFileToHashmap(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        return new Gson().fromJson(reader, HashMap.class);
    }
}
