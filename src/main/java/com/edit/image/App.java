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

        Parent root = FXMLLoader.load(getClass().getResource("/imageEdit.fxml"));
        Scene scene = new Scene(root);
        Canvas canvas = (Canvas) root.getChildrenUnmodifiable().getFirst();
        stage.setScene(scene);
        stage.setTitle("Editor");
        stage.show();

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