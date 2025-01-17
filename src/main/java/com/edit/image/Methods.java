package com.edit.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Methods {


    public void saveImage(Canvas canvas){
        FileChooser fileChooser = new FileChooser();
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(sp, null), null), "png", fileChooser.showSaveDialog(null));
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }


    public void quit(Stage stage, Canvas canvas){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText("Your changes were not saved");
            alert.setContentText("Do you want to save before exiting?");
            if(alert.showAndWait().get() == ButtonType.OK){
                saveImage(canvas);
            }
            stage.close();
    }


    public void newProject(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/imageEdit.fxml"));
            Scene scene = new Scene(root);
            Canvas canvas = (Canvas) root.getChildrenUnmodifiable().getFirst();
            stage.setScene(scene);
            stage.setTitle("Editor");
            stage.show();
            stage.setOnCloseRequest(e -> quit(stage, canvas));
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public void openProject(GraphicsContext gc) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Project");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                Image image = new Image(inputStream);
                gc.drawImage(image, 0, 0, image.getWidth(), image.getHeight());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }

}
