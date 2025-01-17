package com.edit.image;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ImageEditController implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private MenuItem editImage;

    @FXML
    private Menu editMenu;

    @FXML
    private Menu effectsMenu;

    @FXML
    private CheckBox eraser;

    @FXML
    private Menu fileMenu;

    @FXML
    private Menu helpMenu;

    @FXML
    private MenuItem insertImage;

    @FXML
    private MenuBar menuBar;

    @FXML
    private MenuItem newFileMenu;

    @FXML
    private MenuItem openFileMenu;

    @FXML
    private MenuItem quitFileMenu;

    @FXML
    private MenuItem saveFileMenu;

    @FXML
    private Spinner<Integer> spinner;

    @FXML
    private ToolBar toolBar;

    double[] lastX = {0};
    double[] lastY = {0};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100));

        //desenhar no canvas
        canvas.setOnMouseDragged(e -> {
            double size = (double)(spinner.getValue());
            double x = e.getX();
            double y = e.getY();
        
            if (eraser.isSelected()) {
                gc.clearRect(x - size / 2, y - size / 2, size, size);
            } else {
                gc.setStroke(colorPicker.getValue());
                gc.setLineWidth(size);
                gc.strokeLine(lastX[0], lastY[0], x, y); // Desenha uma linha entre os pontos
            }
        
            // Atualiza as posições anteriores
            lastX[0] = x;
            lastY[0] = y;
        });
        
        // Configuração do evento para pressionar o mouse
        canvas.setOnMousePressed(e -> {
            lastX[0] = e.getX();
            lastY[0] = e.getY();
        });

        //salvar imagem do canvas
        saveFileMenu.setOnAction(e -> Methods.saveImage(canvas));

        //sair com menu
        quitFileMenu.setOnAction(e -> {
            e.consume();
            Stage stage = (Stage)((Node) e.getSource()).getScene().getWindow();
            Methods.quit(stage, canvas);
        });

    }

}
