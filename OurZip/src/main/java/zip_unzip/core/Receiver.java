package zip_unzip.core;

import Utils.CheckCode.CRC32CheckSum;
import ui.core.Data;
import zip_unzip.LibUnzip;
import zip_unzip.LibZip;
import zip_unzip.ManUnzip;
import zip_unzip.ManZip;

import javax.swing.*;

public class Receiver {
    /**
     * @param data
     * @param sender
     * @return 0:success; -1:Unknown error; 1:src path error; 2:target path error; 3:special level
     * @return
     */
    static public int receive(Data data, int sender) {
        int signal;
        String Path;
        if (data.compression_level == 10)
        {
            ManZip ManualZip=new ManZip(data.srcPath, data.targetPath);
            signal =ManualZip.IsLegal();
            if(signal==1||signal==3)return signal;
            if (signal == 2) {
                if (JOptionPane.showConfirmDialog(null,
                        "Target File is already exists.\nDo you want REPLACE it?", "Confirm Dialog",
                        JOptionPane.YES_NO_OPTION) == 0) {
                    signal = 0;
                    ManualZip.DeletTar();
                }
            }
            if(signal!=0)return 2;
            ManualZip.Zip();
            Path=ManualZip.getTargetFile().getAbsolutePath();
        }
        else if (sender == 1 || sender == 2)
        {
            LibZip LibraryZip = new LibZip(data.srcPath, data.targetPath, data.compression_level);
            signal = LibraryZip.IsLegal();
            if (signal == 1) return signal;
            if (signal == 2) {
                if (JOptionPane.showConfirmDialog(null,
                        "Target File is already exists.\nDo you want REPLACE it?", "Confirm Dialog",
                        JOptionPane.YES_NO_OPTION) == 0) {
                    signal = 0;
                    LibraryZip.DeletTar();
                }
            }
            if (signal != 0) return signal;
            LibraryZip.Zip();
            Path=LibraryZip.getTargetFile().getAbsolutePath();
        }
        else if (data.srcPath.charAt(data.srcPath.length() - 4) == 'o')
        {
            ManUnzip.Call(data.srcPath, data.targetPath);
            Path=data.srcPath;
        }
        else
        {
            LibUnzip.Call(data.srcPath,data.targetPath);
            Path=data.srcPath;
        }

        if(data.check==false)return 0;

        if(data.check_type=="SHAcode")data.target_check_sum="TODO";//TODO
        else{
            if(data.check_type=="CRC32Code")data.target_check_sum= CRC32CheckSum.CRC32Code(Path);
            else data.target_check_sum= CRC32CheckSum.CRC32BISCode(Path);
        }

        return 0;
    }
}
