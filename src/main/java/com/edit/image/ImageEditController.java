package com.edit.image;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import javafx.scene.paint.Color;
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
    private Menu filtersMenu;

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

    private List<Filter> filters = Arrays.asList(
            new Filter("Invert", c -> c.invert()),
            new Filter("Grayscale", c -> c.grayscale()),
            new Filter("Black and White", c -> valueOf(c) < 1.5 ? Color.BLACK : Color.WHITE),
            new Filter("Red", c -> Color.color(1.0, c.getGreen(), c.getBlue())),
            new Filter("Green", c -> Color.color(c.getRed(), 1.0, c.getBlue())),
            new Filter("Blue", c -> Color.color(c.getRed(), c.getGreen(), 1.0))
    );

    private double valueOf(Color c) {
        return c.getRed() + c.getGreen() + c.getBlue();
    }

    private Methods methods = new Methods();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
            1, 100, 6
        ));

        //adicionar filtros ao menu
        filters.forEach(filter -> {
            MenuItem item = new MenuItem(filter.name);
            item.setOnAction(e -> methods.applyFilter(gc, filter, canvas));
            filtersMenu.getItems().add(item);
        });
        
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
        saveFileMenu.setOnAction(e -> methods.saveImage(canvas));

        newFileMenu.setOnAction(e -> methods.newProject());

        openFileMenu.setOnAction(e -> methods.openProject(gc));

        //sair com menu
        quitFileMenu.setOnAction(e -> {
            e.consume();
            Stage stage = (Stage) canvas.getScene().getWindow();
            methods.quit(stage, canvas);
        });

    }

}
