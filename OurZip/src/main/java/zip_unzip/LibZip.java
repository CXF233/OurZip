package zip_unzip;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class LibZip {
    private File srcFile, targetFile;
    private ZipOutputStream out;
    int level;

    public File getTargetFile() {
        return targetFile;
    }

    public LibZip(String srcPath, String targetPath, int level) {
        this.srcFile = new File(srcPath);
        int len = srcFile.getName().lastIndexOf('.');
        if (len == -1)
            len = srcFile.getName().length();
        this.targetFile = new File(targetPath, srcFile.getName().substring(0, len) + ".zip");
        this.level=level;
    }

    /**
     * secure srcFile exactly exists and target the opposite
     *
     * @return 0:legal 1:src doesn't exist 2:target is occupied
     */
    public int IsLegal() {
        if (!srcFile.exists()||!srcFile.isFile()) return 1;
        if (targetFile.exists()) return 2;

        return 0;
    }
    public void DeletTar(){
        targetFile.delete();
    }

    /**
     * create a ZipOutputStream and set compression level
     *
     * @return true:success
     */
    public boolean Zip() {
        try (FileOutputStream targetfilestream = new FileOutputStream(targetFile)) {
            out = new ZipOutputStream(targetfilestream,null);
            if(level!=-1)
                out.setLevel(level);
            FileTypeDiv(srcFile, "");
            out.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * branch a File into file or directory, zip depends on its type
     *
     * @param file file to divide type
     * @param basedir directory of current File
     */
    private void FileTypeDiv(File file, String basedir) {
        if (file.isFile()) ZipFile(file, basedir);
        else ZipDirectory(file, basedir);
        return;
    }

    /**
     * set a new entry and write data collected from srcfile to out
     *
     * @param srcfile file to be compressed
     * @param basedir directory of current file
     */
    private void ZipFile(File srcfile, String basedir) {
        System.out.println("||package:UI.core | Class:ZipCore | Method:ZipFile() Working");

        try (
                FileInputStream in = new FileInputStream(srcfile);
        ) {
            out.putNextEntry(new ZipEntry(basedir + srcfile.getName()));
            byte[] buf = new byte[1024];
            for (int len = in.read(buf); len > 0; len = in.read(buf)) {
                out.write(buf, 0, len);
            }
            out.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("||package:UI.core | Class:ZipCore | Method:ZipFile() End");
        return;
    }

    /**
     * enumerate each file in srcdir and compress
     * @param srcdir current dir
     * @param baseddir dir of current dir
     */
    private void ZipDirectory(File srcdir, String baseddir) {
        System.out.println("||package:UI.core | Class:ZipCore | Method:ZipDirectory() Working");

        String basedir = baseddir + srcdir.getName() + "/";
        for (File file :
                srcdir.listFiles()) {
            FileTypeDiv(file, basedir);
        }

        System.out.println("||package:UI.core | Class:ZipCore | Method:ZipDirectory() End");
        return;
    }

}
