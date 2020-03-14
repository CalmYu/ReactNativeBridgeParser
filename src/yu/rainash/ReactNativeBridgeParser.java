package yu.rainash;

import org.apache.commons.io.FileUtils;
import yu.rainash.bridge.ReactNativePackage;

import java.io.File;
import java.io.IOException;

/**
 * Created by yu.rainash
 */
public class ReactNativeBridgeParser {

    public static ReactNativePackage parseFromAAR(File aarFile) throws IOException {
        File jarFile = new File(aarFile.getParent(), "__classes__.jar");
        AARExtractor.extractClassesJar(aarFile.getAbsolutePath(), jarFile.getAbsolutePath());
        ReactNativePackage ret = new BridgeCollector().collectBridgeInfo(jarFile);
        FileUtils.forceDelete(jarFile);
        return ret;
    }

    public static ReactNativePackage parseFromJar(File jarFile) throws IOException {
        return new BridgeCollector().collectBridgeInfo(jarFile);
    }

}
