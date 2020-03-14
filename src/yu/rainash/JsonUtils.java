package yu.rainash;

import com.google.gson.Gson;

/**
 * Created by yu.rainash
 */
public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> cls) {
        return GSON.fromJson(json, cls);
    }

}