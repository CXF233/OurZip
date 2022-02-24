package ui.core;

import zip_unzip.core.Receiver;

public class Data {
    public String srcPath,targetPath,src_Check_Code,check_type,target_check_sum;
    public int compression_level;
    public boolean check;

    public Data(String srcPath, String targetPath, boolean check, String check_type, int compression_level, String src_Check_Code) {
        this.srcPath = srcPath;
        this.targetPath = targetPath;
        this.check = check;
        this.check_type = check_type;
        this.compression_level = compression_level;
        this.src_Check_Code = src_Check_Code;
    }

    static public int Call(Data data, int sender){
        return Receiver.receive(data,sender);
    }
}
