package com.edit.image;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

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
}
