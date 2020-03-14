package yu.rainash.bridge;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yu.rainash
 */
public class ReactNativePackage {

    public List<BridgeModule> modules = new ArrayList<>();

    public void addModule(BridgeModule info) {
        modules.add(info);
    }

}