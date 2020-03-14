package yu.rainash;

import java.util.Collection;

/**
 * Created by yu.rainash
 */
public class Utils {

    public static boolean isEmptyCollection(Collection collection) {
        return collection == null || collection.size() == 0;
    }

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase().concat(text.substring(1));
    }

}