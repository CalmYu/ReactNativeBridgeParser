package yu.rainash;

import yu.rainash.bridge.ReactNativePackage;

import java.io.File;

/**
 * Created by yu.rainash
 */
public class MainClass {

    public static void main(String[] args) throws Exception {
        File aarFile = new File("./test/demo.aar");
        ReactNativePackage rp = ReactNativeBridgeParser.parseFromAAR(aarFile);
        System.out.println(JsonUtils.toJson(rp));
    }

}
