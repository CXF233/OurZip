package Utils;

import javafx.scene.control.Alert;

public class AlertMessage {
    private Alert warning;
    private Alert error;
    private Alert success;

    public AlertMessage() {
        warning=new Alert(Alert.AlertType.WARNING);
        warning.setTitle("WARNING");
        warning.setHeaderText(null);
        error=new Alert(Alert.AlertType.ERROR);
        error.setTitle("ERROR");
        error.setHeaderText(null);
        success=new Alert(Alert.AlertType.INFORMATION);
        success.setTitle("Done!");
        success.setHeaderText(null);
    }
    public void Warning(String message){
        warning.setContentText(message);
        warning.showAndWait();
    }
    public void Error(String message){
        error.setContentText(message);
        error.showAndWait();
    }
    public void Success(String message){
        success.setContentText(message);
        success.showAndWait();
    }
}
