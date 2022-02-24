package zip_unzip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LibUnzip {
    static private String targetPath;
    /**
     * produce a zipfile from path and consume
     *
     * @return true for success
     */
    static public boolean Call(String srcPath, String targetPath) {
        if (targetPath.charAt(targetPath.length() - 1) != '/')
            LibUnzip.targetPath = targetPath + File.separator;
        else
            LibUnzip.targetPath = targetPath;

        try (ZipFile zipFile = new ZipFile(srcPath)) {
            zipFile.stream()
                    .forEach(ze -> {
                        try {
                            ExtracSingleFile(zipFile.getInputStream(ze), ze);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * create file dirs and write data
     *
     * @param in compressed file data stored as InputSteam
     * @param ze current zip entry, contains its relative path
     * @return true for success
     */
    static private boolean ExtracSingleFile(InputStream in, ZipEntry ze) {
        String path = LibUnzip.targetPath + ze.getName();
        System.out.println(path);

        new File(path.substring(0, path.lastIndexOf("/"))).mkdirs();
        try (
                FileOutputStream out = new FileOutputStream(path);
        ) {
            byte[] buf = new byte[1024];
            for (int len = in.read(buf); len > 0; len = in.read(buf)) {
                out.write(buf, 0, len);
            }
        } catch (Exception e) {
            return false;
        }
        try { in.close(); } catch (IOException e) {
            return false;
        }
        return true;
    }
}
