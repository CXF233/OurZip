package zip_unzip;

import zip_unzip.HuffmanCompress.HFMCompression;

import java.io.File;

public class ManZip {
    private String srcPath, targetPath;
    private File srcFile, targetFile;

    public File getTargetFile() {
        return targetFile;
    }

    public ManZip(String srcPath, String targetPath) {
        this.srcPath = srcPath;
        this.targetPath = targetPath;
        this.srcFile=new File(this.srcPath);
    }

    public int IsLegal() {
        if (!srcFile.exists()||!srcFile.isFile()) return 1;

        int len=srcFile.getName().lastIndexOf('.');
        String filetype = new String(srcFile.getName().substring(len+1));
        if(!filetype.equals("txt"))return 3;

        targetFile=new File(targetPath,srcFile.getName().substring(0, len) + ".ozip");
        if (targetFile.exists()) return 2;

        return 0;
    }
    public void DeletTar(){
        targetFile.delete();
    }

    public boolean Zip(){
        new HFMCompression().HFMZip(srcFile.getAbsolutePath(),targetFile.getAbsolutePath());
        return true;
    }
}
