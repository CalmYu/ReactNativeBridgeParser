package yu.rainash;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by yu.rainash
 */
public class AARExtractor {

    private static final String CLASSES_JAR_NAME = "classes.jar";

    /**
     * extract classes.jar from aar file
     */
    public static void extractClassesJar(String aarPath, String destJarPath) {
        ZipFile zipFile = null;
        try {
            zipFile = new ZipFile(aarPath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (CLASSES_JAR_NAME.equals(entry.getName())) {
                    InputStream ins = zipFile.getInputStream(entry);
                    FileUtils.copyInputStreamToFile(ins, new File(destJarPath));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zipFile);
        }
    }

}