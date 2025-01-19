package com.edit.image;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ImageEditController implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private MenuItem removeImage;

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

    @FXML
    private ScrollPane scrollPane;

    double[] lastX = {0};
    double[] lastY = {0};

    private List<DraggableImage> images = new ArrayList<>();
    private DraggableImage selectedImage = null;
    private WritableImage canvasSnapshot;


    public void insertImage(GraphicsContext gc) {
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile != null) {
            try (FileInputStream inputStream = new FileInputStream(selectedFile)) {
                Image image = new Image(inputStream);
                DraggableImage draggableImage = new DraggableImage(image, 0, 0);
                images.add(draggableImage);
    
                // Verifica se a imagem excede as dimensões do Canvas
                double newWidth = draggableImage.getX() + image.getWidth();
                double newHeight = draggableImage.getY() + image.getHeight();
    
                // Chama expandCanvas para ajustar o tamanho do Canvas se necessário
                if (newWidth > canvas.getWidth() || newHeight > canvas.getHeight()) {
                    methods.expandCanvas(canvas, newWidth, newHeight);
                }
    
                saveCanvasState();
                redrawCanvas(gc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } 
    }
    

    private void saveCanvasState() {
        canvasSnapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, canvasSnapshot);
    }

    private void redrawCanvas(GraphicsContext gc) {
        if (canvasSnapshot != null) {
            gc.drawImage(canvasSnapshot, 0, 0);
        }
        for (DraggableImage image : images) {
            gc.drawImage(image.getImage(), image.getX(), image.getY());
        }
    }
    

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

            // Verifique se o desenho ultrapassou o tamanho do Canvas
            if (x > canvas.getWidth() || y > canvas.getHeight()) {
                methods.expandCanvas(canvas, Math.max(x, canvas.getWidth()), Math.max(y, canvas.getHeight()));
            }

            if (selectedImage != null && !eraser.isSelected()) {
                selectedImage.setX(x - selectedImage.getImage().getWidth() / 2);
                selectedImage.setY(y - selectedImage.getImage().getHeight() / 2);
                redrawCanvas(gc);
            }
        
            // Atualiza as posições anteriores
            lastX[0] = x;
            lastY[0] = y;
        });
        
        // Configuração do evento para pressionar o mouse
        canvas.setOnMousePressed(e -> {
            lastX[0] = e.getX();
            lastY[0] = e.getY();
            double mouseX = e.getX();
            double mouseY = e.getY();
            selectedImage = null;

            // Verifica se o clique está sobre uma imagem
            for (DraggableImage image : images) {
                double x = image.getX();
                double y = image.getY();
                double width = image.getImage().getWidth();
                double height = image.getImage().getHeight();

                if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height) {
                    selectedImage = image;
                    break;
                }
            }
        });

        // Configuração do evento para soltar o mouse
        canvas.setOnMouseReleased(e -> {
            if (selectedImage != null) {
                selectedImage.updateImage(canvas);
            }
        });


        //salvar imagem do canvas
        saveFileMenu.setOnAction(e -> methods.saveImage(canvas));

        newFileMenu.setOnAction(e -> methods.newProject());

        openFileMenu.setOnAction(e -> methods.openProject(gc));

        insertImage.setOnAction(e -> insertImage(gc));

        removeImage.setOnAction(e -> removeImage());
        
                //sair com menu
                quitFileMenu.setOnAction(e -> {
                    e.consume();
                    Stage stage = (Stage) canvas.getScene().getWindow();
                    methods.quit(stage, canvas);
                });
        
    }
        
        
    private void removeImage() {
        if (selectedImage != null) {
            images.remove(selectedImage);
            selectedImage = null; 
            GraphicsContext gc = canvas.getGraphicsContext2D();
            redrawCanvas(gc);
        }
    }
        
        
    public Canvas getCanvas() {
        return this.canvas;
    }

}
