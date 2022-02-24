package zip_unzip;

import zip_unzip.HuffmanCompress.HFMCompression;

public class ManUnzip {
    static public void Call(String srcPath, String targetPath) {
        new HFMCompression().HFMUnZip(srcPath, targetPath);
    }
}
