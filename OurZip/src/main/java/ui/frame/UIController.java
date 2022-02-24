package ui.frame;

import Utils.AlertMessage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Translate;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import ui.core.Data;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class UIController implements Initializable {
    //region Pane
    @FXML
    private AnchorPane root;
    //endregion

    //region Controls
    //region Controls in TAB 'File Zip'
    @FXML
    private TextField File_Path_Src, File_Path_Target;
    @FXML
    private Button File_FC_Src, File_FC_Target;
    @FXML
    private CheckBox File_Check;
    @FXML
    private ChoiceBox File_Check_Type, File_Compression_Level;
    @FXML
    private Button File_Confirm;
    //endregion

    //region Controls in TAB 'Dir Zip'
    @FXML
    private TextField Dir_Path_Src, Dir_Path_Target;
    @FXML
    private Button Dir_FC_Src, Dir_FC_Target;
    @FXML
    private CheckBox Dir_Check;
    @FXML
    private ChoiceBox Dir_Check_Type, Dir_Compression_Level;
    @FXML
    private Button Dir_Confirm;
    //endregion

    //region Controls in TAB 'Unzip'
    @FXML
    private TextField Unzip_Path_Src, Unzip_Path_Target;
    @FXML
    private Button Unzip_FC_Src, Unzip_FC_Target;
    @FXML
    private CheckBox Unzip_Check;
    @FXML
    private ChoiceBox Unzip_Check_Type;
    @FXML
    private TextField Unzip_Check_src;
    @FXML
    private Button Unzip_Confirm;
    //endregion
    //endregion

    //region Actions
    //region Actions in TAB 'File Zip'
    @FXML
    protected void on_File_FC_Src_Click() {
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            File_Path_Src.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a File.");
        }
    }

    @FXML
    protected void on_File_FC_Target_Click() {
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null) {
            File_Path_Target.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a Directory.");
        }
    }

    @FXML
    protected void on_File_Confirm_Click() {
        int compression_level;
        if(File_Compression_Level.getValue()=="Default")compression_level=-1;
        else compression_level= (int) File_Compression_Level.getValue();
        Data data=new Data(
                File_Path_Src.getText(),File_Path_Target.getText(),
                File_Check.isSelected(),
                (String) File_Check_Type.getValue(),
                compression_level,
                Unzip_Check_src.getText()
        );
        int signal= Data.Call(data,1);
        Translate(signal);
        if(signal==0&&data.check)alert.Success("Check Sum is: "+data.target_check_sum);
    }
//endregion

    //region Actions in TAB 'Dir Zip'
    @FXML
    protected void on_Dir_FC_Src_Click() {
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null) {
            Dir_Path_Src.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a Directory.");
        }
    }

    @FXML
    protected void on_Dir_FC_Target_Click() {
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null) {
            Dir_Path_Target.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a Directory.");
        }
    }

    @FXML
    protected void on_Dir_Confirm_Click() {
        int compression_level;
        if(Dir_Compression_Level.getValue()=="Default")compression_level=-1;
        else compression_level= (int) Dir_Compression_Level.getValue();
        Data data=new Data(
                Dir_Path_Src.getText(),Dir_Path_Target.getText(),
                Dir_Check.isSelected(),
                (String) Dir_Check_Type.getValue(),
                compression_level,
                Unzip_Check_src.getText()
        );
        int signal= Data.Call(data,2);
        Translate(signal);
        if(signal==0&&data.check)alert.Success("Check Sum is: "+data.target_check_sum);
    }
//endregion

    //region Actions in TAB 'Unzip'
    @FXML
    protected void on_UnZip_FC_Src_Click() {
        File file = zipChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            Unzip_Path_Src.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a File.");
        }
    }

    @FXML
    protected void on_UnZip_FC_Target_Click() {
        File file = directoryChooser.showDialog(root.getScene().getWindow());
        if (file != null) {
            Unzip_Path_Target.setText(file.getPath());
        } else {
            alert.Warning("Please Choose a Directory.");
        }
    }

    @FXML
    protected void on_UnZip_Confirm_Click() {
        Data data=new Data(
                Unzip_Path_Src.getText(),Unzip_Path_Target.getText(),
                Unzip_Check.isSelected(),
                (String) Unzip_Check_Type.getValue(),
                -2,
                Unzip_Check_src.getText()
        );
        int signal= Data.Call(data,3);
        Translate(signal);
        if(signal==0&&data.check)alert.Success("Check Sum is: "+data.target_check_sum);
    }
//endregion
    //endregion

    //region Objects
    private AlertMessage alert;
    private FileChooser fileChooser, zipChooser;
    private DirectoryChooser directoryChooser;
    //endregion

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //region Create Objects
        alert = new AlertMessage();
        List list = new ArrayList();

        fileChooser = new FileChooser();
        zipChooser = new FileChooser();
        zipChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Zip", "*.zip"),
                new FileChooser.ExtensionFilter("OurZip", "*.ozip"));
        directoryChooser = new DirectoryChooser();
        //endregion

        //region TAB 'File Zip'
        File_Check_Type.getItems().addAll("SHAcode", "CRC32Code", "CRC32BISCode");
        File_Check_Type.setValue("SHAcode");
        File_Check_Type.setDisable(true);
        File_Check.selectedProperty().addListener(
                property -> File_Check_Type.setDisable(!File_Check.isSelected())
        );
        File_Compression_Level.getItems().addAll("Default");
        for (int i = 0; i <= 10; i++) list.add(i);
        File_Compression_Level.getItems().addAll(list);
        File_Compression_Level.setValue("Default");
        //endregion

        //region TAB 'Dir Zip'
        Dir_Check_Type.getItems().addAll("SHAcode", "CRC32Code", "CRC32BISCode");
        Dir_Check_Type.setValue("SHAcode");
        Dir_Check_Type.setDisable(true);
        Dir_Check.selectedProperty().addListener(
                property -> Dir_Check_Type.setDisable(!Dir_Check.isSelected())
        );
        Dir_Compression_Level.getItems().addAll("Default");
        list.remove(10);
        Dir_Compression_Level.getItems().addAll(list);
        Dir_Compression_Level.setValue("Default");
        //endregion

        //region TAB 'Unzip'
        Unzip_Check_Type.getItems().addAll("SHAcode", "CRC32Code", "CRC32BISCode");
        Unzip_Check_Type.setValue("SHAcode");
        Unzip_Check_Type.setDisable(true);
        Unzip_Check.selectedProperty().addListener(
                property -> {
                    Unzip_Check_Type.setDisable(!Unzip_Check.isSelected());
                    Unzip_Check_src.setDisable(!Unzip_Check.isSelected());
                }
        );
        Unzip_Check_src.setText("");
        //endregion

    }

    private void Translate(int signal){
        switch (signal){
            case 1: alert.Error("Shutdown caused by wrong source file/directory.");break;
            case 2: alert.Error("Shutdown caused by wrong destination directory.");break;
            case 3: alert.Warning("Level 10 is only available for (*.txt)file.");break;
            case 0: alert.Success("Operation has been done successfully!");break;
            default:alert.Error("Shutdown caused by unknown fatal error.");break;
        }
    }
}
