package com.edit.image;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class App extends Application{

    @Override
    public void start(Stage stage) throws Exception {   

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/imageEdit.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        ImageEditController iec = loader.getController();
        Canvas canvas = iec.getCanvas();
        stage.setScene(scene);
        stage.setTitle("Editor");
        stage.show();
        stage.setResizable(false);

        Methods methods = new Methods();

        //sair clicando no X
        stage.setOnCloseRequest(e ->{
            e.consume();
            methods.quit(stage, canvas);
        });
        
    }

    public static void main(String[] args) {
        launch(args);
    }
}