package com.edit.image;

import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Methods {

    private CheckBox eraser;
    private Canvas canvas;
    private GraphicsContext gc;
    private ColorPicker colorPicker;
    private TextField penSize;
    
    public Methods(CheckBox eraser, Canvas canvas, GraphicsContext gc, ColorPicker colorPicker, TextField penSize) {
        this.eraser = eraser;
        this.canvas = canvas;
        this.gc = gc;
        this.colorPicker = colorPicker;
        this.penSize = penSize;
    }


    public void drawRects(){
        canvas.setOnMouseDragged(e ->{
            double size = Double.parseDouble(penSize.getText());
            double x = e.getX() - size/2;
            double y = e.getY() - size/2;
            
            if(eraser.isSelected()){
                gc.clearRect(x, y, size, size);
            } else{
                gc.setFill(colorPicker.getValue());
                gc.fillRect(x, y, size, size);    
            }
        });
    }


    public void saveImage(){
        FileChooser fileChooser = new FileChooser();
        SnapshotParameters sp = new SnapshotParameters();
        sp.setFill(Color.TRANSPARENT);
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(canvas.snapshot(null, null), null), "png", fileChooser.showSaveDialog(null));
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }


    public void quit(Stage stage){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Quit");
            alert.setHeaderText("Your changes were not saved");
            alert.setContentText("Do you want to save before exiting?");
            if(alert.showAndWait().get() == ButtonType.OK){
                saveImage();
            } else {
                stage.close();
            }
    }
}
