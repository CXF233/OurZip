module ui.frame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens ui.frame to javafx.fxml;
    exports ui.frame;
}