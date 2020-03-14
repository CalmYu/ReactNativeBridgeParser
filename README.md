# ReactNativeBridgeParser
It can be used in android gradle plugin<br/>
<pre>
import yu.rainash.bridge.ReactNativePackage;

import java.io.File;

public class MainClass {

    public static void main(String[] args) throws Exception {
        File aarFile = new File("./test/demo.aar");
        ReactNativePackage rp = ReactNativeBridgeParser.parseFromAAR(aarFile);
        System.out.println(JsonUtils.toJson(rp));
    }

}

</pre>
