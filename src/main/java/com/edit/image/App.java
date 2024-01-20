package com.edit.image;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application{
    
    private AnchorPane root;

    private Canvas canvas;

    private ColorPicker colorPicker;

    private MenuItem editImage;

    private Menu editMenu;

    private Menu effectsMenu;

    private CheckBox eraser;

    private Menu fileMenu;

    private Menu helpMenu;

    private MenuItem about;

    private MenuItem doc;

    private MenuItem insertImage;

    private MenuBar menuBar;

    private MenuItem newFileMenu;

    private MenuItem openFileMenu;

    private MenuItem quitFileMenu;

    private MenuItem saveFileMenu;

    private TextField penSize;

    private ToolBar toolBar;

    @Override
    public void start(Stage stage) throws Exception {

        //inicia root
        root = new AnchorPane();
        root.setPrefSize(900, 650);

        
        //inicia canvas e graficos
        canvas = new Canvas(600, 600);
        canvas.setLayoutX(14);
        canvas.setLayoutY(36);
        canvas.setCursor(Cursor.CROSSHAIR);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();


        //toolbar
        toolBar = new ToolBar();
        toolBar.setPrefSize(171, 600);
        toolBar.setOrientation(Orientation.VERTICAL);
        toolBar.setLayoutX(715);
        toolBar.setLayoutY(36);
        root.getChildren().add(toolBar);
    

        //filhos da toolbar
        //colorPicker
        colorPicker = new ColorPicker();
        colorPicker.setCursor(Cursor.OPEN_HAND);
        //penSize
        penSize = new TextField();
        penSize.setCursor(Cursor.TEXT);
        //eraser
        eraser = new CheckBox("Eraser");
        eraser.setPrefSize(166, 19);
        eraser.setCursor(Cursor.HAND);
        eraser.setFont(Font.font(14));
        //adiciona filhos
        toolBar.getItems().addAll(colorPicker,eraser,penSize);


        //menu de arquivos
        fileMenu = new Menu("File");
        newFileMenu = new MenuItem("New Project");
        openFileMenu = new MenuItem("Open Project");
        saveFileMenu = new MenuItem("Save Project");
        quitFileMenu = new MenuItem("Quit");
        fileMenu.getItems().addAll(newFileMenu,openFileMenu,saveFileMenu,quitFileMenu);

        //menu de efeitos
        effectsMenu = new Menu("Effects");

        //menu de edição
        editMenu = new Menu("Edit");
        insertImage = new MenuItem("Insert Image");
        editImage = new MenuItem("Edit Image");
        editMenu.getItems().addAll(insertImage,editImage);

        //menu de ajuda
        helpMenu = new Menu("Help");
        about = new MenuItem("About");
        doc = new MenuItem("Documentation");
        helpMenu.getItems().addAll(about,doc);

        //menubar
        menuBar = new MenuBar(fileMenu,effectsMenu,editMenu,helpMenu);
        menuBar.setPrefSize(900, 28);
        root.getChildren().add(menuBar);

        //janela
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Editor");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}