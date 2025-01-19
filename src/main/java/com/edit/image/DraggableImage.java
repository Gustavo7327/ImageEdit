package com.edit.image;

import java.nio.IntBuffer;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;

public class DraggableImage {

    private Image image;
    private double x;
    private double y;

    public DraggableImage(Image image, double x, double y) {
        this.image = image;
        this.x = x;
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }


    public void updateImage(Canvas canvas) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
    
        // Verifique se a posição da imagem está dentro do limite do canvas
        if (x < 0 || y < 0 || x + image.getWidth() > canvas.getWidth() || y + image.getHeight() > canvas.getHeight()) {
            // Caso as coordenadas estejam fora dos limites do canvas, retorne para evitar o erro
            System.out.println("Coordenadas fora dos limites do canvas");
            return;
        }
    
        // Captura o PixelReader do canvas
        WritableImage fullCanvasSnapshot = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, fullCanvasSnapshot);
    
        int imageWidth = (int) image.getWidth();
        int imageHeight = (int) image.getHeight();
    
        // Criar um buffer para armazenar os pixels da área correspondente à imagem
        IntBuffer buffer = IntBuffer.allocate(imageWidth * imageHeight);
    
        // Obter os pixels da área da imagem no canvas, garantindo que o valor da coordenada x e y seja válido
        fullCanvasSnapshot.getPixelReader().getPixels(
            (int) x, (int) y, imageWidth, imageHeight,
            PixelFormat.getIntArgbInstance(),
            buffer, imageWidth
        );
    
        // Criar uma nova WritableImage com os pixels capturados
        WritableImage updatedImage = new WritableImage(imageWidth, imageHeight);
        updatedImage.getPixelWriter().setPixels(
            0, 0, imageWidth, imageHeight,
            PixelFormat.getIntArgbInstance(),
            buffer.array(), 0, imageWidth
        );
    
        // Atualizar a imagem armazenada no DraggableImage
        this.image = updatedImage;
    
        // Redesenha o canvas após atualizar a imagem
        gc.clearRect(x, y, imageWidth, imageHeight);
        gc.drawImage(updatedImage, x, y);
    }
    

}

