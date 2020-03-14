package yu.rainash;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by yu.rainash
 */
public class JarUtils {

    /**
     * unzip a jar file
     */
    public static void unJar(File jarFile, File targetDir) throws IOException {
        targetDir.mkdirs();
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                File entryDir = new File(targetDir, entry.getName());
                entryDir.mkdirs();
            } else {
                File entryFile = new File(targetDir, entry.getName());
                FileUtils.copyInputStreamToFile(jar.getInputStream(entry), entryFile);
            }
        }
        jar.close();
    }

}