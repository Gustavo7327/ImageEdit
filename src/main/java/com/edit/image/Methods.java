package com.edit.image;

import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Methods {


    public static void saveImage(Canvas canvas){
        FileChooser fileChooser = new FileChooser();
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(sp, null), null), "png", fileChooser.showSaveDialog(null));
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }


    public static void quit(Stage stage, Canvas canvas){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText("Your changes were not saved");
            alert.setContentText("Do you want to save before exiting?");
            if(alert.showAndWait().get() == ButtonType.OK){
                saveImage(canvas);
            }
            stage.close();
    }
}
