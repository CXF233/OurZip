package Utils.CheckCode;

import java.io.*;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;

public class CRC32CheckSum {

    /**
     * 用于在压缩文件时计算文件的CRC32校验码
     * 传入参数为文件的绝对路径字符串
     * 返回值为文件的CRC32校验字符串
     */
    public static String CRC32Code(String src) {
        try {
            return getCRC32String(src);
        }catch (IOException e){
            return "-1";
        }

    }

    /**
     * 用于在解压文件时计算文件CRC32校验码，并与用户输入的源文件校验码进行比对
     * 传入参数为压缩包绝对路径、源文件校验码
     * 返回值为boolean类型的值，表示校验码相同或不同
     */
    public static boolean CompareCRC32Code(String src,String crc32Code) throws IOException {
        boolean res = crc32Code.equals(CRC32Code(src));
        return res;
    }

    /**
     * 用于在压缩文件时计算文件的CRC32BIS校验码
     * 传入参数为文件的绝对路径字符串
     * 返回值为文件的CRC32BIS校验码(Long)
     */
    public static String CRC32BISCode(String src) {
        try {
            return String.valueOf(checksumBufferedInputStream(src));
        }catch (IOException e){
            return "-1";
        }
    }

    /**
     * 采用BufferedInputStream的方式加载文件
     */
    public static long checksumBufferedInputStream(String filepath) throws IOException {
        InputStream inputStream = new BufferedInputStream(new FileInputStream(filepath));
        CRC32 crc = new CRC32();
        byte[] bytes = new byte[1024];
        int cnt;
        while ((cnt = inputStream.read(bytes)) != -1) {
            crc.update(bytes, 0, cnt);
        }
        inputStream.close();
        return crc.getValue();
    }

    /**
     * 使用CheckedInputStream计算CRC
     */
    public static Long getCRC32(String filepath) throws IOException {
        CRC32 crc32 = new CRC32();
        FileInputStream fileinputstream = new FileInputStream(new File(filepath));
        CheckedInputStream checkedinputstream = new CheckedInputStream(fileinputstream, crc32);
        while (checkedinputstream.read() != -1) {
        }
        checkedinputstream.close();
        return crc32.getValue();
    }

    /**
     * 将CRC32的值转成字符串形式
     */
    public static String getCRC32String(String filepath) throws IOException {
        Long CRC32Code = getCRC32(filepath);
        return Long.toHexString(CRC32Code);
    }
}
